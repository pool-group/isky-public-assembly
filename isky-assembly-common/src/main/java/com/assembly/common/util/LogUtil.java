package com.assembly.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 规范化日志打印工具，注意日志的级别选择：<br>
 *
 * @author ken.ny
 * @version Id: LogUtil.java, v 0.1 2018年11月02日 下午16:20 ken.ny Exp $
 */
@Slf4j(topic = "common_service")
public class LogUtil {

    /** 摘要日志的内容分隔符 */
    public static final String SEP       = ",";

    /** 摘要日志的内容分隔符 */
    public static final String MARK       = " >>>>>>>>>>>>>>>>>>>>> ";

    /** 修饰符 */
    private static final char  RIGHT_TAG = ']';

    /** 修饰符 */
    private static final char  LEFT_TAG  = '[';

    /** 完成 */
    private static final String  SUCCESS = " FINISH ";


    /**
     * 打印error日志。
     *
     * ERROR日志记录尽量使用{@link}，避免日志的重复记录
     *
     * @param logger    日志对象
     * @param objs      任意个要输出到日志的参数
     */
    public static void error(Logger logger,Throwable e, Object... objs) {
        logger.error(getLogString(objs),e);
    }

    public static void error(Logger logger, Object... objs) {
        logger.error(getLogString(objs));
    }

    public static void error(Throwable e,Object... objs) {
        log.error(getLogString(objs),e);
    }

    public static void error(Object... objs) {
        log.error(getLogString(objs));
    }

    /**
     * 打印warn日志。
     *
     * ERROR日志记录尽量使用{@link}，避免日志的重复记录
     *
     * @param logger    日志对象
     * @param objs      任意个要输出到日志的参数
     */
    public static void warn(Logger logger, Object... objs) {
        logger.warn(getLogString(objs));
    }

    public static void warn(Object... objs) {
        log.warn(getLogString(objs));
    }

    /**
     * 打印warn日志。
     *
     * ERROR日志记录尽量使用{@link}，避免日志的重复记录
     *
     * @param logger    日志对象
     * @param objs      任意个要输出到日志的参数
     */
    public static void info(Logger logger, Object... objs) {
        logger.info(getLogInfoString(objs));
    }

    public static void info(Object... objs) {
        log.info(getLogInfoString(objs));
    }

    /**
     * 生成输出到日志的字符串
     *
     * @param objs      任意个要输出到日志的参数
     * @return          日志字符串
     */
    public static String getLogString(Object... objs) {
        StringBuilder log = new StringBuilder();
        log.append(LEFT_TAG);
        // 预留扩展位
        log.append(MARK).append(RIGHT_TAG);

        if (objs != null) {
            for (Object o : objs) {
                log.append(o);
            }
        }
        return log.toString();
    }

    /**
     * 生成输出到日志的字符串
     *
     * @param objs      任意个要输出到日志的参数
     * @return          日志字符串
     */
    public static String getLogInfoString(Object... objs) {
        StringBuilder log = new StringBuilder();
        log.append(LEFT_TAG);
        // 预留扩展位
        log.append(SUCCESS).append(RIGHT_TAG);

        if (objs != null) {
            for (Object o : objs) {
                log.append(o);
            }
        }
        return log.toString();
    }


}
