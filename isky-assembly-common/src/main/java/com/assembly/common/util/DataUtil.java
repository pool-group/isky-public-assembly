package com.assembly.common.util;

import com.assembly.common.model.tuple.Tuple2x;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据处理工具类
 *
 * @author ken.ny
 * @version Id: DataUtil.java, v 0.1 2018年11月07日 下午18:56 ken.ny Exp $
 */
public class DataUtil {

    /**26字母*/
    protected final static char[] CHARACTER = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    /**0-9数字*/
    protected final static int[] NUMBER     = {0,1,2,3,4,5,6,7,8,9};

    /**类型0：字母  1：数字*/
    protected final static int[] TYPE       ={0,1};

    /**长度0：纯字母  1：纯数字 2：字母数字混合*/
    protected final static int[] NAME_TYPE  ={0,1,2};

    /**
     * 功能：产生min-max中的n个不重复的随机数 [min,max) 左开右闭
     *
     * min:产生随机数的其实位置
     * mab：产生随机数的最大位置
     * n: 所要产生多少个随机数
     *
     */
    public static Integer[] randomNumber(int min, int max, int n){

        //判断是否已经达到索要输出随机数的个数
        if(n>(max-min+1) || max <min){
            return null;
        }

        Integer[] result = new Integer[n]; //用于存放结果的数组

        int count = 0;
        while(count <n){
            int num = (int)(Math.random()*(max-min))+min;
            boolean flag = true;
            for(int j=0;j<count;j++){
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }


    /**
     * 动态获取机器人名称
     *
     * @param numberSize 名字长度
     * @return
     */
    public static String getRandomUserName(int numberSize){

        StringBuilder sb=new StringBuilder();
        int nameType=NAME_TYPE[randomNumber(0,3,1)[0]];
        for(int i=0;i<numberSize;i++){
            switch (nameType){
                case 0:
                    sb.append(String.valueOf(CHARACTER[randomNumber(0,26,1)[0]]));
                    break;
                case 1:
                    sb.append(String.valueOf(NUMBER[randomNumber(0,10,1)[0]]));
                    break;
                case 2:
                    int type=TYPE[randomNumber(0,2,1)[0]];
                    switch (type){
                        case 0:
                            sb.append(String.valueOf(CHARACTER[randomNumber(0,26,1)[0]]));
                            break;
                        case 1:
                            sb.append(String.valueOf(NUMBER[randomNumber(0,10,1)[0]]));
                            break;
                    }
                    break;
            }
        }
        return sb.toString();
    }


    /**
     * 生成19位不重复UID
     *
     * @return
     */
    public static Long createSequenceUid(){

        Integer[] randomNumber=DataUtil.randomNumber(0,10,4);
        return Long.valueOf(Calendar.getInstance().get(Calendar.YEAR)+""+(System.currentTimeMillis()+"")+(randomNumber[0].longValue()+"")+(randomNumber[1].longValue()+""));
    }


    /**
     * Method call examples and parameter descriptions
     *
     * List<int[]> allValue=Lists.newArrayList();
     * DataUtil.exec(Arrays.stream(montageCard).boxed().collect(Collectors.toList()),"",0,3,allValue);
     *
     * @param list
     * @param prefix
     * @param index
     * @param limit
     * @param result
     */
    public static void exec(List<Integer> list, String prefix, int index, int limit, List<int[]> result) {
        if ((limit-1)==StringUtils.countMatches(prefix,"#")) {
            String[] arrays=prefix.split("#");
            int[] p=new int[limit];
            int inx=limit;
            for (String s:arrays){
                p[p.length-inx]= Integer.parseInt(s);
                inx--;
            }
            result.add(p);
        }
        for (int i = index; i < list.size(); i++) {
            List<Integer> tmp = new LinkedList<>(list);
            if(prefix.split("#").length>limit){
                continue;
            }
            if(!"".equals(prefix)&&!prefix.endsWith("#"))
                prefix=prefix+"#";
            exec(tmp, prefix+tmp.remove(i),i,limit,result);
        }
    }

//    public static void main(String[] args) {
//        List<int[]> allValue1=Lists.newArrayList();
//        List<int[]> allValue2=Lists.newArrayList();
//        List<int[]> allValue3=Lists.newArrayList();
//        List<int[]> allValue4=Lists.newArrayList();
//        List<int[]> result=Lists.newArrayList();
//        List<Integer> list=Lists.newArrayList(1,2,3,4,5);
//        exec(list,"",0,2,allValue1);
//        exec(list,"",0,3,allValue2);
//        exec(list,"",0,4,allValue3);
//        exec(list,"",0,5,allValue4);
//
//        result.addAll(allValue1);
//        result.addAll(allValue2);
//        result.addAll(allValue3);
//        result.addAll(allValue4);
//
//        System.out.println();
//    }

    /**
     * Merge two arrays
     *
     * @param var1
     * @param var2
     * @return
     */
    public static int[] mergeArray(int[] var1,int[] var2){
        int[] result=new int[var1.length+var2.length];
        System.arraycopy(var1,0,result,0,var1.length);
        System.arraycopy(var2,0,result,var1.length,var2.length);
        return result;
    }

    public static Tuple2x<Boolean,String> ifInclude(List<String> list,String str){
        for(int i=0;i<list.size();i++){
            if(list.get(i).indexOf(str)!=-1) {
                return new Tuple2x<Boolean,String>(true,list.get(i));
            }
        }
        return new Tuple2x<Boolean,String>(false,"NULL");
    }

}
