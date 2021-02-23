package com.assembly.common.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @ClassName: MybatisPersistence
 * @Description: Mybatis持久层组件
 * @author: k.y
 * @date: 2021年02月20日 4:35 下午
 */
public class MybatisPersistence<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    public MybatisPersistence(){}

    public MybatisPersistence(M baseMapper, Class<T> entityClass, Class<M> mapperClass){
        super.baseMapper=baseMapper;
        super.entityClass=entityClass;
        super.mapperClass=mapperClass;
    }
}
