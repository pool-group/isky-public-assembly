package com.assembly.common.util;

import com.assembly.common.exception.BizRuntimeException;
import com.assembly.common.model.ExcMessageModel;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;

/**
 * *断言工具类。
 *
 * <p><b>使用方法：</b>
 * <ul>
 * <li>在本地定义AssertUtil的匿名bean。
 * <li>给匿名bean注入属性exceptionClassName的值。
 * <li>exceptionClass必须是IpayCommonException的子类，且实现带错误描述参数的构造方法。
 * </ul>
 *
 * @author ken.ny
 * @version Id: AssertUtil.java, v 0.1 2019年01月28日 下午15:38 ken.ny Exp $
 */
@SuppressWarnings("rawtypes")
public class AssertUtil {

    /** 异常类Class名称 */
    private static String      exceptionClassName;

    /** 异常对象构造方法 */
    private static Constructor constructor;

    /**
     * 断言表达式的值为true，否则抛出指定错误信息。
     *
     * @param expValue          断言表达式
     * @param model             异常消息
     * @param objs              任意个异常描述信息的参数
     */
    public static void isTrue(final boolean expValue, final ExcMessageModel model,
                              final Object... objs) {
        if (expValue) {
            String resultMsg=getLogString(objs);
            if(StringUtils.isBlank(resultMsg))
                throw new BizRuntimeException(model);
            else {
                throw new BizRuntimeException(model,resultMsg);
            }
        }
    }

    /**
     * 断言表达式的值为true，否则抛出指定错误信息。
     *
     * @param expValue          断言表达式
     * @param objs              任意个异常描述信息的参数
     */
    public static void isTrue(final boolean expValue, final Object... objs) {
        if (expValue) {
            throw new BizRuntimeException(getLogString(objs));
        }
    }

    /**
     * 生成输出到日志的字符串
     *
     * @param objs      任意个要输出到日志的参数
     * @return          日志字符串
     */
    private static String getLogString(Object... objs) {
        StringBuilder log = new StringBuilder();

        for (Object o : objs) {
            log.append(o);
        }
        return log.toString();
    }

}
