package com.assembly.common.util;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * Bean Copy工具类
 * @author powell
 * @version:1.0.0
 */
@Slf4j
public class BeanCopier {

    private static MapperFacade mapper = null;
    private static MapperFactory mapperFactory = null;

    private static final String SERIAL_VERSION_ID = "serialVersionUID";

    static {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapper = mapperFactory.getMapperFacade();

    }

    /**
     * 註册 classMapBuilder
     */
    public static void register(ClassMapBuilder classMapBuilder) {
        mapperFactory.registerClassMap(classMapBuilder);
    }

    /**
     * 拷贝一个对象为另一个类型
     *
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S source, Class<T> clazz) {
        return mapper.map(source, clazz);
    }

    /**
     * 对象属性拷贝
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S source, T target) {
        mapper.map(source, target);
        return target;
    }

    /**
     * 集合拷贝
     *
     * @param sourceList
     * @param clazz
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> clazz) {
        return mapper.mapAsList(sourceList, clazz);
    }

    /**
     * 对象转换成Map
     * @param clazz 对象
     * @param object 实例化对象
     * @return
     */
    public static Map<String, Object> classToMap(Class clazz, Object object) {
        Map<String, Object> map = new HashMap<>();
        List<Field> allFields = new ArrayList<>();
        allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Class clazzSuper = clazz.getSuperclass();
        while (clazzSuper != Object.class) {
            allFields.addAll(Arrays.asList(clazzSuper.getDeclaredFields()));
            clazzSuper = clazzSuper.getSuperclass();
        }
        allFields.stream().forEach(field -> {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                if(!fieldName.equals(SERIAL_VERSION_ID)) {
                    Object fieldVal = field.get(object);
                    map.put(fieldName, fieldVal);
                }
            } catch (IllegalAccessException e) {

            }
        });
        return map;
    }

    /**
     * 将map集合中的数据转化为指定对象的同名属性中 公共方法
     *
     * @param clazz 实体类
     * @param map   map 集合
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T convertMapToClass(Class<T> clazz, Map<Object, Object> map) {
        T bean = null;
        try {
            bean = clazz.newInstance();
            List<Field> allFields = new ArrayList<>();
            allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            Class clazzSuper = clazz.getSuperclass();
            while (clazzSuper != Object.class) {
                allFields.addAll(Arrays.asList(clazzSuper.getDeclaredFields()));
                clazzSuper = clazzSuper.getSuperclass();
            }
            T finalBean = bean;
            allFields.stream().forEach(field -> {
                //取消默认 Java 语言访问控制检查的能力 即允许对private属性进行直接赋值或获取
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!fieldName.equals(SERIAL_VERSION_ID)) {
                    try {
                        Object obj = map.get(fieldName);
                        //Long类型需手动转换，否则报类型转换出错
                        if (field.getType() == Long.class) {
                            Long lValue = null != obj ? Long.valueOf(obj.toString()) : null;
                            field.set(finalBean, lValue);
                        } else if (field.getType() == BigDecimal.class) {
                            BigDecimal bValue = null != obj ? BigDecimal.valueOf(Double.valueOf(obj.toString())) : null;
                            field.set(finalBean, bValue);
                        } else {
                            field.set(finalBean, obj);
                        }
                    } catch (IllegalAccessException e) {
                        log.error("map转class时异常信息：{}",e);
                    }
                }
            });
        } catch (IllegalAccessException e) {
            log.error("map转class时异常信息：{}",e);
        } catch (InstantiationException e) {
            log.error("map转class时异常信息：{}",e);
        }

        return bean;
    }
}
