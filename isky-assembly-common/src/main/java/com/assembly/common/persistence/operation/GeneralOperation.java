package com.assembly.common.persistence.operation;

import com.assembly.common.persistence.MongoDBPersistence;
import com.assembly.common.persistence.abstracts.AbstractOpCallback;
import com.assembly.common.persistence.common.Contants;
import com.assembly.common.persistence.common.StopWatchExtend;
import com.assembly.common.persistence.model.GeneralPresent;
import com.assembly.common.persistence.page.PageResult;
import com.assembly.common.persistence.service.MService;
import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadPreference;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: GeneralOperation
 * @Description: 常规API
 * @author: k.y
 * @date: 2021年02月22日 5:22 下午
 */
@Slf4j
public class GeneralOperation implements MongoOperations {

    private final static String WARN_MESSAGE = "This method The current Does not support, Please modify the method yourself or Use this.getMongoTemplate() original method!";
    private final static String WARN_NOT_SUPPORT_SHARD_KEY = "insertAll(Collection<? extends T> collection) Does not support shardKey!";

    private GeneralPresent present;
    private MongoTemplate mongo;
    private MongoDBPersistence.TartDB tartDB;
    private MService mService;

    public GeneralOperation(GeneralPresent generalPresent, MService mService) {
        this.mService = mService;
        this.present = generalPresent;
    }

    /**
     * 可扩展统一监控, 如:Prometheus
     *
     * @param function
     * @param collectionName
     * @param objects
     * @param <R>
     * @return
     */
    public <R> R doFunction(AbstractOpCallback<String, R> function, String collectionName, Object... objects) {
        StopWatchExtend stopWatch = new StopWatchExtend();
        stopWatch.start("doFunction(AbstractOpCallback<P, R> function)");
        Arrays.stream(objects).forEach(this::updatePresent);
        present.setCollectionName(collectionName);
        this.tartDB = mService.getTartDB(present);
        this.mongo = this.tartDB.getCurrentMongoTemplate();
        R r = function.call(present.getCollectionName());
        stopWatch.stop();
        if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
            stopWatch.setId("collectionName:" + present.getCollectionName() + "|doFunction() Time Consuming|" + "parameter: " + present.toString());
            log.info(stopWatch.prettyPrint());
        }
        return r;
    }

    private void updatePresent(Object obj) {
        if (obj instanceof Class) {
            present.setEntityClass((Class) obj);
        } else if (obj instanceof Query) {
            present.setQuery((Query) obj);
        } else {
            present.setExtend(obj);
        }
    }

    /**
     * 扩展findOne(Query query), 并支持分库分表
     *
     * @param query
     * @param <T>
     * @return
     */
    public <T> T findOne(Query query) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return (T) mongo.findOne(query, present.getEntityClass(), name);
            }
        }, null, query);
    }

    /**
     * 扩展find(Query query), 并支持分库分表
     *
     * @param query
     * @param <T>
     * @return
     */
    public <T> List<T> find(Query query) {
        return doFunction(new AbstractOpCallback<String, List<T>>() {
            @Override
            public List<T> call(String name) {
                return mongo.find(query, present.getEntityClass(), name);
            }
        }, null, query);
    }

    /**
     * 扩展count(Query query), 并支持分库分表
     *
     * @param query
     * @return
     */
    public long count(Query query) {
        return doFunction(new AbstractOpCallback<String, Long>() {
            @Override
            public Long call(String name) {
                return mongo.count(query, present.getEntityClass(), name);
            }
        }, null, query);
    }

    /**
     * 扩展findPage, 并支持分库分表
     *
     * @param query
     * @return
     */
    public <T> PageResult<T> findPage(Query query){
        return doFunction(new AbstractOpCallback<String, PageResult<T>>() {
            @Override
            public PageResult<T> call(String name) {
                StopWatchExtend stopWatch = new StopWatchExtend();
                stopWatch.start("mongo.count(query, present.getEntityClass(), name)");
                int total= (int) mongo.count(query, present.getEntityClass(), name);
                stopWatch.stop();
                stopWatch.start("mongo.find(query, present.getEntityClass(), name)");
                List<T> data= mongo.find(query, present.getEntityClass(), name);
                stopWatch.stop();
                if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
                    stopWatch.setId("collectionName:" + present.getCollectionName() + "|findPage(Query query) Time Consuming|" + "parameter: " + present.toString());
                    log.info(stopWatch.prettyPrint());
                }
                return new PageResult(total, data);
            }
        }, null, query);
    }

    /**
     * 扩展findPage, 并支持分库分表
     *
     * @param query
     * @param aClass
     * @return
     */
    public <T> PageResult<T> findPage(Query query, Class<?> aClass){
        return doFunction(new AbstractOpCallback<String, PageResult<T>>() {
            @Override
            public PageResult<T> call(String name) {
                StopWatchExtend stopWatch = new StopWatchExtend();
                stopWatch.start("mongo.count(query, aClass, name)");
                int total= (int) mongo.count(query, aClass, name);
                stopWatch.stop();
                stopWatch.start("mongo.find(query, aClass, name)");
                List<T> data= (List<T>) mongo.find(query, aClass, name);
                stopWatch.stop();
                if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
                    stopWatch.setId("collectionName:" + present.getCollectionName() + "|findPage(Query query, Class<?> aClass) Time Consuming|" + "parameter: " + present.toString());
                    log.info(stopWatch.prettyPrint());
                }
                return new PageResult(total, data);
            }
        }, null, query, aClass);
    }

    /**
     * 扩展findPage, 并支持分库分表
     *
     * @param query
     * @param aClass
     * @param collectionName
     * @return
     */
    public <T> PageResult<T> findPage(Query query, Class<?> aClass, String collectionName){
        return doFunction(new AbstractOpCallback<String, PageResult<T>>() {
            @Override
            public PageResult<T> call(String name) {
                StopWatchExtend stopWatch = new StopWatchExtend();
                stopWatch.start("mongo.count(query, aClass, name)");
                int total= (int) mongo.count(query, aClass, name);
                stopWatch.stop();
                stopWatch.start("mongo.find(query, aClass, name)");
                List<T> data= (List<T>) mongo.find(query, aClass, name);
                stopWatch.stop();
                if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
                    stopWatch.setId("collectionName:" + present.getCollectionName() + "|findPage(Query query, Class<?> aClass) Time Consuming|" + "parameter: " + present.toString());
                    log.info(stopWatch.prettyPrint());
                }
                return new PageResult(total, data);
            }
        }, collectionName, query, aClass);
    }

    @Override
    public String getCollectionName(Class<?> aClass) {
        return doFunction(new AbstractOpCallback<String, String>() {
            @Override
            public String call(String obj) {
                return mongo.getCollectionName(aClass);
            }
        }, null, aClass);
    }

    @Override
    public Document executeCommand(String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public Document executeCommand(Document document) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public Document executeCommand(Document document, ReadPreference readPreference) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public void executeQuery(Query query, String s, DocumentCallbackHandler documentCallbackHandler) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T execute(DbCallback<T> dbCallback) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T execute(Class<?> aClass, CollectionCallback<T> collectionCallback) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T execute(String s, CollectionCallback<T> collectionCallback) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public SessionScoped withSession(ClientSessionOptions clientSessionOptions) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public MongoOperations withSession(ClientSession clientSession) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> aClass, CollectionOptions collectionOptions) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public MongoCollection<Document> createCollection(String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public MongoCollection<Document> createCollection(String s, CollectionOptions collectionOptions) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public Set<String> getCollectionNames() {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public MongoCollection<Document> getCollection(String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> boolean collectionExists(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public boolean collectionExists(String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> void dropCollection(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public void dropCollection(String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public IndexOperations indexOps(String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public IndexOperations indexOps(Class<?> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public ScriptOperations scriptOps() {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode bulkMode, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode bulkMode, Class<?> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode bulkMode, Class<?> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> findAll(Class<T> aClass) {
        return doFunction(new AbstractOpCallback<String, List<T>>() {
            @Override
            public List<T> call(String name) {
                return mongo.findAll(aClass, name);
            }
        }, null, aClass);
    }

    @Override
    public <T> List<T> findAll(Class<T> aClass, String collectionName) {
        return doFunction(new AbstractOpCallback<String, List<T>>() {
            @Override
            public List<T> call(String name) {
                return mongo.findAll(aClass, name);
            }
        }, null, aClass);
    }

    @Override
    public <T> GroupByResults<T> group(String s, GroupBy groupBy, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> GroupByResults<T> group(Criteria criteria, String s, GroupBy groupBy, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> typedAggregation, String s, Class<O> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> typedAggregation, Class<O> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> aClass, Class<O> aClass1) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String s, Class<O> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> typedAggregation, String s, Class<O> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> typedAggregation, Class<O> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, Class<?> aClass, Class<O> aClass1) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, String s, Class<O> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(String s, String s1, String s2, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(String s, String s1, String s2, MapReduceOptions mapReduceOptions, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String s, String s1, String s2, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String s, String s1, String s2, MapReduceOptions mapReduceOptions, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> GeoResults<T> geoNear(NearQuery nearQuery, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> GeoResults<T> geoNear(NearQuery nearQuery, Class<T> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T findOne(Query query, Class<T> aClass) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.findOne(query, aClass, name);
            }
        }, null, query, aClass);
    }

    @Override
    public <T> T findOne(Query query, Class<T> aClass, String collectionName) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.findOne(query, aClass, name);
            }
        }, collectionName, query, aClass);
    }

    @Override
    public boolean exists(Query query, String s) {
        return false;
    }

    @Override
    public boolean exists(Query query, Class<?> aClass) {
        return false;
    }

    @Override
    public boolean exists(Query query, Class<?> aClass, String s) {
        return false;
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> find(Query query, Class<T> aClass) {
        return doFunction(new AbstractOpCallback<String, List<T>>() {
            @Override
            public List<T> call(String name) {
                return mongo.find(query, aClass, name);
            }
        }, null, query, aClass);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> aClass, String collectionName) {
        return doFunction(new AbstractOpCallback<String, List<T>>() {
            @Override
            public List<T> call(String name) {
                return mongo.find(query, aClass, name);
            }
        }, collectionName, query, aClass);
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T findById(Object id, Class<T> aClass) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.findById(id, aClass, name);
            }
        }, null, id, aClass);
    }

    @Override
    public <T> T findById(Object id, Class<T> aClass, String collectionName) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.findById(id, aClass, name);
            }
        }, collectionName, id, aClass);
    }

    @Override
    public <T> List<T> findDistinct(Query query, String s, Class<?> aClass, Class<T> aClass1) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> List<T> findDistinct(Query query, String s, String s1, Class<?> aClass, Class<T> aClass1) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, Class<T> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions findAndModifyOptions, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions findAndModifyOptions, Class<T> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <S, T> T findAndReplace(Query query, S s, FindAndReplaceOptions findAndReplaceOptions, Class<S> aClass, String s1, Class<T> aClass1) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T findAndRemove(Query query, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> T findAndRemove(Query query, Class<T> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param query
     * @param aClass
     * @return
     */
    @Override
    public long count(Query query, Class<?> aClass) {
        return doFunction(new AbstractOpCallback<String, Long>() {
            @Override
            public Long call(String name) {
                return mongo.count(query, aClass, name);
            }
        }, null, query, aClass);
    }

    @Override
    public long count(Query query, String collectionName) {
        return doFunction(new AbstractOpCallback<String, Long>() {
            @Override
            public Long call(String name) {
                return mongo.count(query, name);
            }
        }, collectionName, query);
    }

    @Override
    public long count(Query query, Class<?> aClass, String collectionName) {
        return doFunction(new AbstractOpCallback<String, Long>() {
            @Override
            public Long call(String name) {
                return mongo.count(query, aClass, name);
            }
        }, collectionName, query, aClass);
    }

    @Override
    public <T> T insert(T objectToSave) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.insert(objectToSave, name);
            }
        }, null, objectToSave);
    }

    @Override
    public <T> T insert(T objectToSave, String collectionName) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.insert(objectToSave, name);
            }
        }, collectionName, objectToSave);
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param aClass
     * @param <T>
     * @return
     */
    @Override
    public <T> Collection<T> insert(Collection<? extends T> collection, Class<?> aClass) {
        return doFunction(new AbstractOpCallback<String, Collection<T>>() {
            @Override
            public Collection<T> call(String name) {
                return mongo.insert(collection, name);
            }
        }, null, collection, aClass);
    }

    @Override
    public <T> Collection<T> insert(Collection<? extends T> collection, String collectionName) {
        return doFunction(new AbstractOpCallback<String, Collection<T>>() {
            @Override
            public Collection<T> call(String name) {
                return mongo.insert(collection, name);
            }
        }, collectionName, collection);
    }

    @Override
    public <T> Collection<T> insertAll(Collection<? extends T> collection) {
        return doFunction(new AbstractOpCallback<String, Collection<T>>() {
            @Override
            public Collection<T> call(String name) {
                if (null != present.getShardKey()) {
                    throw new RuntimeException(WARN_NOT_SUPPORT_SHARD_KEY);
                }
                return mongo.insertAll(collection);
            }
        }, null, collection);
    }

    /**
     * 扩展参数, 以支持分库分表
     *
     * @param objectToSave
     * @param <T>
     * @return
     */
    @Override
    public <T> T save(T objectToSave) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.save(objectToSave, name);
            }
        }, null, objectToSave);
    }

    @Override
    public <T> T save(T objectToSave, String collectionName) {
        return doFunction(new AbstractOpCallback<String, T>() {
            @Override
            public T call(String name) {
                return mongo.save(objectToSave, name);
            }
        }, collectionName, objectToSave);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, Class<?> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, Class<?> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, Class<?> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, Class<?> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult updateMulti(Query query, Update update, Class<?> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult updateMulti(Query query, Update update, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public UpdateResult updateMulti(Query query, Update update, Class<?> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public DeleteResult remove(Object o) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public DeleteResult remove(Object o, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public DeleteResult remove(Query query, Class<?> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public DeleteResult remove(Query query, Class<?> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public DeleteResult remove(Query query, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> aClass, String s) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public MongoConverter getConverter() {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> ExecutableAggregation<T> aggregateAndReturn(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> ExecutableFind<T> query(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> ExecutableInsert<T> insert(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> MapReduceWithMapFunction<T> mapReduce(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> ExecutableRemove<T> remove(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

    @Override
    public <T> ExecutableUpdate<T> update(Class<T> aClass) {
        throw new RuntimeException(WARN_MESSAGE);
    }

}
