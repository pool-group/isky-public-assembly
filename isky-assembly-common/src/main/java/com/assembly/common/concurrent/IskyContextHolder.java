package com.assembly.common.concurrent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * com.assembly.common.concurrent
 *
 * 业务上下文的Holder类
 *
 * @author k.y
 * @version Id: WebhomeContextHolder.java, v 0.1 2020年07月07日 10:59 k.y Exp $
 */
public class IskyContextHolder {

    /** 本地线程 */
    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<>();

    private IskyContextHolder() {
        // 禁用构造函数
    }


    /**
     * 初始化业务上下文
     */
    public static void init() {
        if (local.get() == null) {
            local.set(new HashMap<String, Object>());
        }
    }

    /**
     * 增加属性
     *
     * @param key   属性<code>key</code>
     * @param value 属性<code>value</code>
     *
     */
    public static ThreadLocal addProperty(String key, Object value) {
        if (StringUtils.isEmpty(key)) {
            throw new NullPointerException("key parameter is blank");
        }
        Map<String, Object> properties = local.get();
        if (properties == null) {
            properties = new HashMap<>();
            local.set(properties);
        }
        properties.put(key, value);
        return local;
    }

    /**
     * 获取属性
     *
     * @param key
     * @return 属性
     */
    public static Object getProperty(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Map<String, Object> properties = local.get();
        if (CollectionUtils.isEmpty(properties)) {
            return null;
        }
        return properties.get(key);
    }

    /**
     * 清除当前线程上下文
     */
    public static void clear() {
        local.remove();
    }
}
