package com.assembly.common.mongo;

import com.assembly.common.idwork.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author powell
 */

@Configuration
@ConditionalOnProperty(name = "mongodb.config.collection-shard-size")
@Slf4j
public class MultipleMongoTemplate implements InitializingBean {

    private List<MongoTemplate> mongoTemplateList = new ArrayList<>();
    private Integer databaseShardSize = 1;
    private Integer collectionShardSize = 1;

    private static ThreadLocal<Long> SHARD_ID = new ThreadLocal<>();
    private Map<Class, String> CLAZZ_CNAME_MAP = new ConcurrentHashMap<>();

    @Autowired
    private MultipleMongoPropertiesAutoConfig autoConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> addressList = autoConfig.getCluster().getAddress();

        for (String addr : addressList) {
            List<String> tempAddressList = new ArrayList<>();
            tempAddressList.add(addr);

            MongoProperties mongoProperties = autoConfig.getCluster();
            mongoProperties.setAddress(tempAddressList);

            MongoDbFactory mongoDbFactory = MongoConfig.build(mongoProperties).mongoDbFactory();
            MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
            mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
            mongoTemplateList.add(new MongoTemplate(mongoDbFactory, mappingMongoConverter));

        }

        databaseShardSize = mongoTemplateList.size();
        collectionShardSize = autoConfig.getCollectionShardSize();

//        if(databaseShardSize < collectionShardSize){
//            log.error("databaseShardSize:{} is less  than collectionShardSize:{}",databaseShardSize,collectionShardSize);
//            throw new Exception("databaseShardSize is less than collectionShardSize ");
//        }
    }

    /**
     * 获取所有 数据库 mongotemplate 列表
     *
     * @return
     */
    public List<MongoTemplate> getMongoTemplateList() {
        return mongoTemplateList;
    }

    /**
     * 查询所有 分表表名
     *
     * @param clazz
     * @return
     */
    public List<String> getCNameList(Class clazz) {
        String collectionName = getCollectionName(clazz);
        return getCNameList(collectionName);
    }

    /**
     * 查询所有分表 表名
     *
     * @param collectionName
     * @return
     */
    public List<String> getCNameList(String collectionName) {
        List<String> cNameList = new ArrayList<>();
        for (int i = 0; i < collectionShardSize; i++) {
            cNameList.add(cName(collectionName, i));
        }
        return cNameList;
    }

    /**
     * 根据id获取DB索引
     *
     * @param originalId
     * @return
     */
    public Integer getDbIndexByOriginalId(Long originalId) {
        Long shardId = IdUtils.getShardId(originalId);
        return getDbIndex(shardId);
    }

    /**
     * 根据id获取shardId进行分片
     *
     * @param originalId
     * @return
     */
    public MongoTemplate selectDB(Long originalId) {
        Long shardId = IdUtils.getShardId(originalId);
        return selectDBByShardId(shardId);
    }

    /**
     * 保证db 均衡
     *
     * @param shardId
     * @return
     */
    public Integer getDbIndex(long shardId) {
        return (int) shardId % databaseShardSize;
    }

    private Integer getCollectionIndex(long shardId) {
        return (int) (shardId / databaseShardSize % collectionShardSize);
    }

    /**
     * 根据shardid进行分片
     *
     * @param shardId 分片键的值
     * @return
     */
    public MongoTemplate selectDBByShardId(Long shardId) {
        Integer dbIndex = getDbIndex(shardId);
        SHARD_ID.set(shardId);
        return mongoTemplateList.get(dbIndex);
    }

    public String cName(String collectionName) throws Exception {
        Long shardId = SHARD_ID.get();
        if (shardId == null) {
            throw new Exception("shardId is null");
        }

        Integer tableSuffix = getCollectionIndex(shardId);
        SHARD_ID.remove();

        return cName(collectionName, tableSuffix);
    }

    /**
     * 根据 class 查询分表名称
     *
     * @param clazz
     * @return
     * @throws Exception
     */
    public String cName(Class clazz) throws Exception {
        String collectionName = getCollectionName(clazz);
        return cName(collectionName);
    }


    public String cName(String collectionName, Integer tableSuffix) {
        return collectionName + tableSuffix;
    }

    /**
     * 根据class 查询 mongodb document表名
     *
     * @param clazz
     * @return
     */
    private String getCollectionName(Class clazz) {
        String collectionName = CLAZZ_CNAME_MAP.get(clazz);

        boolean notCached = StringUtils.isEmpty(collectionName);
        Annotation annotation = clazz.getDeclaredAnnotation(Document.class);

        if (notCached && annotation != null) {
            collectionName = ((Document) annotation).value();
            if (StringUtils.isEmpty(collectionName)) {
                collectionName = ((Document) annotation).collection();
            }
        }
        if (StringUtils.isEmpty(collectionName)) {
            collectionName = clazz.getSimpleName();
        }
        if (!notCached) {
            CLAZZ_CNAME_MAP.put(clazz, collectionName);
        }

        return collectionName;
    }

    /**
     * 查询 db分片数量
     *
     * @return
     */
    public Integer getDatabaseShardSize() {
        return this.databaseShardSize;
    }

    /**
     * 查询 分表数量
     *
     * @return
     */
    public Integer getCollectionShardSize() {
        return this.collectionShardSize;
    }

    public static void main(String[] args) {

        int databaseShardSize = 16;
        int collectionShardSize = 4;

        int cnt = 256;

        for (int i = 0; i < cnt; i++) {
            //
            Long shardId = IdUtils.getShardId(i);
            int dbIdx = (int) (shardId % databaseShardSize);

            Integer tableSuffix = (int) (shardId / databaseShardSize % collectionShardSize);

            System.out.println("i:" + i + " db:" + dbIdx + " table:" + tableSuffix);
        }

    }
}
