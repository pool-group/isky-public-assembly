package com.assembly.common.persistence;

import com.assembly.common.mongo.MultipleMongoTemplate;
import com.assembly.common.persistence.model.Present;
import com.assembly.common.persistence.service.MService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * @ClassName: MongoDBPersistence
 * @Description: MongoDB持久层组件
 * @author: k.y
 * @date: 2021年02月20日 9:16 下午
 */
public abstract class MongoDBPersistence<TDocument> implements MService<TDocument> {

    private MultipleMongoTemplate multipleMongoTemplate;
    private MongoTemplate mongoTemplate;
    private Class<TDocument> modelClass;
    private String collectionName;

    public MongoTemplate getMongoTemplateByShardKey(Long shardKey) {
        return (null == shardKey) ? getMongoTemplate() : getMongoTemplate(shardKey);
    }

    public MongoDBPersistence setMultipleMongoTemplate(MultipleMongoTemplate multipleMongoTemplate) {
        this.multipleMongoTemplate = multipleMongoTemplate;
        return this;
    }

    public MongoDBPersistence setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        return this;
    }

    public MongoDBPersistence setDocumentClass(Class<TDocument> documentClass) {
        this.modelClass = documentClass;
        this.collectionName = this.mongoTemplate.getCollectionName(documentClass);
        return this;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public MongoTemplate getMongoTemplate(Long shardKey) {
        return multipleMongoTemplate.selectDB(shardKey);
    }

    public String getCollectionName(Class<?> documentClass) {
        return getMongoTemplate().getCollectionName(documentClass);
    }

    @SneakyThrows
    public String getCollectionShardName(Class<?> documentClass) {
        return multipleMongoTemplate.cName(documentClass);
    }

    @SneakyThrows
    public String getCollectionShardName(String documentName) {
        return multipleMongoTemplate.cName(documentName);
    }

    public String getDefaultCollectionName() {
        return collectionName;
    }

    public Class<TDocument> getDefaultModelClass() {
        return modelClass;
    }

    public Class<TDocument> currentModelClass() {
        Class<TDocument> tClass = (Class<TDocument>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    @Override
    public TartDB getTartDB(Present present){
        TartDB tartDB=new TartDB();
        tartDB.setCurrentDocumentClass(Optional.ofNullable(present.getEntityClass()).orElse(getDefaultModelClass()));
        tartDB.setCurrentMongoTemplate(getMongoTemplateByShardKey(present.getShardKey()));
        if(null==present.getCollectionName()){
            tartDB.setCurrentCollectionName((null == present.getShardKey())
                    ? getCollectionName(tartDB.getCurrentDocumentClass())
                    : getCollectionShardName(tartDB.getCurrentDocumentClass()));
        }else {
            tartDB.setCurrentCollectionName((null == present.getShardKey())
                    ? present.getCollectionName()
                    : getCollectionShardName(present.getCollectionName()));
        }
        present.setEntityClass(tartDB.getCurrentDocumentClass());
        present.setCollectionName(tartDB.getCurrentCollectionName());
        return tartDB;
    }

    @Autowired
    public void setField(MultipleMongoTemplate multipleMongoTemplate, MongoTemplate mongoTemplate) {
        this.multipleMongoTemplate = multipleMongoTemplate;
        this.mongoTemplate = mongoTemplate;
        this.modelClass = currentModelClass();
        this.collectionName = mongoTemplate.getCollectionName(this.modelClass);
    }

    @Data
    public class TartDB {
        private MongoTemplate currentMongoTemplate;
        private Class<TDocument> currentDocumentClass;
        private String currentCollectionName;
    }
}
