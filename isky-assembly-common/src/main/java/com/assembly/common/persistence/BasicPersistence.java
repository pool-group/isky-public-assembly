package com.assembly.common.persistence;

/**
 * @ClassName: BasicPersistence
 * @Description: 持久层集装箱接口
 * @author: k.y
 * @date: 2021年02月23日 3:40 下午
 */
public interface BasicPersistence<T> {

    /**
     * 获取mybatis plus组件
     *
     * @return
     */
    MybatisPersistence mybatis();
    /**
     * 获取MongoDB组件
     *
     * @return
     */
    MongoDBPersistence mongodb();
}
