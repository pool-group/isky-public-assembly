package com.assembly.common.persistence;

import com.assembly.common.mongo.MultipleMongoTemplate;
import com.assembly.common.persistence.service.MServiceImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;

/**
 * @ClassName: PersistenceService
 * @Description: 持久层集装箱实现
 * @author: k.y
 * @date: 2021年02月20日 2:15 下午
 */
public abstract class BasicPersistenceImpl<M extends BaseMapper<T>, T> implements BasicPersistence<T> {

    /**mybatis plus组件*/
    protected MybatisPersistence mybatis;
    /**MongoDB 组件*/
    protected MongoDBPersistence mongodb;
    private MultipleMongoTemplate multipleMongoTemplate;
    private MongoTemplate mongoTemplate;
    private M baseMapper;

    @PostConstruct
    public void structure(){
        this.mybatis=new MybatisPersistence(baseMapper,currentModelClass(),currentMapperClass());
        this.mongodb=new MServiceImpl()
                .setMultipleMongoTemplate(multipleMongoTemplate)
                .setMongoTemplate(mongoTemplate)
                .setDocumentClass(currentModelClass());
    }

    @Autowired
    public void setField(M baseMapper, MultipleMongoTemplate multipleMongoTemplate, MongoTemplate mongoTemplate){
        this.baseMapper=baseMapper;
        this.multipleMongoTemplate=multipleMongoTemplate;
        this.mongoTemplate=mongoTemplate;
    }

    protected Class<T> currentMapperClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), 0);
    }

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), 1);
    }

    @Override
    public MybatisPersistence mybatis() {
        return mybatis;
    }

    @Override
    public MongoDBPersistence mongodb() {
        return mongodb;
    }
}

