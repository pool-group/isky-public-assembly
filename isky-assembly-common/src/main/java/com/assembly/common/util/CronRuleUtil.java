package com.assembly.common.util;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * Cron Rule表达式工厂解析
 *
 * @author k.y
 * @version Id: CronRuleUtil.java, v 0.1 2018年11月30日 下午11:10 k.y Exp $
 */
@Slf4j
public class CronRuleUtil {


    /*
     * Cron Rule合法表达式样例：
     *
     * 多组：0~5=20|5~15=30|15~25=40|25~35=50|35~50=60
     * 单组：35~50=60
     *
     * 描述：单组举例：当风控类型为比例，当前风控参数在35%到50%之间时，赢的事件概率为60%。
     *
     */

    public final static String MARK_A      =   "~";

    public final static String MARK_B      =   "=";

    public final static String MARK_C      =   "|";

    public final static String MARK_D      =   "\\";

    public final static String MARK_E        =   "#";

    public final static String MARK_F        =   "%";

    private final static String PATTERN     =   "{0}~{1}={2}";

    /**
     * cron rule表达式解析, 并映射对应结果
     *
     * @param cronRule
     * @param rateParam
     * @return
     */
    public static BigDecimal resolve(String cronRule, BigDecimal rateParam){

        log.info("解析准备开始.. ,Cron Rule表达式为：cronRule={}",cronRule);
        //是否为正数
        Boolean isPlus=false;
        //正数为盈利状态，负数为亏损状态
        if(rateParam.compareTo(BigDecimal.valueOf(0))>0){
            isPlus=true;
        }
        BigDecimal rate=rateParam.abs();
        Map<String,BigDecimal> resultmap=new HashMap<>();
        resultmap.put("bdResult",new BigDecimal(0));
        List<Map<String,BigDecimal>> lst=new ArrayList<>();
        //判断表达式只有一组的情况
        if(!cronRule.contains(MARK_C)){
            if(!checkAndCompareTo(cronRule,resultmap,rate,lst)){
                log.error("Cron Rule策略表达式解析异常,请查看是否已容错启用默认规则,并检查数据库表达式配置是否有误");
            }
        }else {
            String[] cronArray=cronRule.split(MARK_D+MARK_C);
            for(String cron:cronArray){
                if(!checkAndCompareTo(cron,resultmap,rate,lst)){
                    log.error("Cron Rule策略表达式解析异常,请查看是否已容错启用默认规则,并检查数据库表达式配置是否有误");
                }
            }
        }
        BigDecimal bdResult=resultmap.get("bdResult");
        if(bdResult.compareTo(BigDecimal.valueOf(0))==0){
            log.error("Cron Rule策略表达式脚本配置区间不完整,请检查!");
        }
        return bdResult;
    }

    /**
     * 验证表达式是否合法
     *
     * @param cronRule
     * @return
     */
    private static Boolean checkAndCompareTo(String cronRule,Map<String,BigDecimal> resultmap,BigDecimal rate,List<Map<String,BigDecimal>> lst) {
        if(cronRule.contains(MARK_A)&&cronRule.contains(MARK_B)){
            Map<String,BigDecimal> model=new HashMap<>();
            String oldValue=cronRule;
            String firstStr=cronRule.split(MARK_A)[0];
            String scendStr=cronRule.split(MARK_A)[1].split(MARK_B)[0];
            String threeStr=cronRule.split(MARK_A)[1].split(MARK_B)[1];
            //整数数字校验,非数字类型会抛出异常
            BigDecimal firstBD= new BigDecimal(firstStr);
            BigDecimal threeBD= new BigDecimal(threeStr);
            BigDecimal scendBD= StringUtils.indexOf(scendStr,"*")!=0?new BigDecimal(scendStr):BigDecimal.ZERO;
            if(StringUtils.indexOf(scendStr,"*")==0&&rate.compareTo(firstBD)>=0){
                resultmap.put("bdResult",threeBD);
            }else if(!StringUtils.equals(scendStr,"*")&&rate.compareTo(firstBD)>=0&&rate.compareTo(scendBD)==-1){
                resultmap.put("bdResult",threeBD);
            }
            model.put("firstBD",firstBD);
            model.put("scendBD",scendBD);
            model.put("threeBD",threeBD);
            lst.add(model);
            String newValue= MessageFormat.format(PATTERN ,firstStr,scendStr,threeStr);
            return oldValue.equals(newValue);
        }
        return false;
    }

    /**
     * 表达式转对象
     *
     * @param clazz
     * @param rule 例：0~0.2=0.5##见习代理|0.2~0.5=0.6##代理学徒|0.5~1=0.7##初级代理|1~2=0.8##中级代理|2~5=0.9##高级代理|5~10=1.1##超级代理
     * @return
     */
    public static<T extends CronRuleModel> List<T> ruleToBean(String rule,Class<T> clazz) {

        List<T> lst= Lists.newArrayList();
        String[] rules;
        if(rule.contains(MARK_C)){
            rules= StringUtils.split(rule, MARK_C);
        }else {
            rules=new String[]{rule};
        }
        for(int i=0;i<rules.length;i++){
            try {
                CronRuleModel model=clazz.newInstance();
                model.setLevel(i+1);
                // 0~0.2=0.5##见习代理
                if(rules[i].contains(MARK_E)){
                    String[] crule= StringUtils.split(rules[i], MARK_E);
                    model.setLevelName(crule[1]);
                    setChildRule(crule[0], model);
                }else {
                    setChildRule(rules[0], model);
                }
                lst.add((T) model);
            } catch (Exception e) {
                log.error("CronRule表达式转对象出现异常",e);
            }
        }
        log.info("CronRule表达式转对象结果：lst={}",lst);
        return lst;
    }

    private static void setChildRule(String str, CronRuleModel model){
        // 0~0.2=0.5
        if(str.contains(MARK_B)){
            String[] ccrule= StringUtils.split(str, MARK_B);
            model.setRate(ccrule[1]+MARK_F);
            // 0~0.2
            if (ccrule[0].contains(MARK_A)){
                String[] cccrule= StringUtils.split(ccrule[0], MARK_A);
                model.setLower(cccrule[0]);
                model.setUpper(cccrule[1]);
            }else {
                log.error("CronRule表达式不正确,缺少 "+MARK_A+" 符号");
            }
        }else {
            log.error("CronRule表达式不正确,缺少 "+MARK_B+" 符号");
        }
    }

    /**
     * 解析获取满足计算的表达式
     * 解析前：0~0.2=0.5#见习代理#|0.2~0.5=0.6#代理学徒#|0.5~1=0.7#初级代理#|1~2=0.8#中级代理#|2~5=0.9#高级代理#
     * 解析后：0~0.2=0.5|0.2~0.5=0.6|0.5~1=0.7|1~2=0.8|2~5=0.9
     *
     * @param rule
     * @return
     */
    public static String getOriginalRule(String rule){
        String[] rules;
        if(rule.contains(MARK_C)){
            rules= StringUtils.split(rule, MARK_C);
        }else {
            rules=new String[]{rule};
        }
        StringBuilder sb=new StringBuilder();
        Arrays.stream(rules).forEach(s -> {
            if(s.contains(MARK_E)){
                sb.append(s.replaceAll(MARK_E+".*"+MARK_E, "")).append(MARK_C);
            }
        });
        return sb.toString().substring(0,sb.toString().length()-1);
    }

    /**
     * 表达式属性抽象
     */
    @Data
   public static class CronRuleModel {

        /**用于计算的表达式*/
        private String originalRule;
        /*
         * 上限下限范例：0 < x <10
         */
        /**上限*/
        private String upper;

        /**下限*/
        private String lower;

        /**计算后金额结果*/
        private BigDecimal brokerage;

        /**计算百分比*/
        private String rate;

        /**级别*/
        private Integer level;

        /**级别名称*/
        private String levelName;
    }
}


