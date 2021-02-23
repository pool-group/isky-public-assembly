package com.assembly.common.persistence.service;

import com.assembly.common.persistence.MongoDBPersistence;
import com.assembly.common.persistence.abstracts.AbstractOpCallback;
import com.assembly.common.persistence.common.Contants;
import com.assembly.common.persistence.common.StopWatchExtend;
import com.assembly.common.persistence.model.BsonPresent;
import com.assembly.common.persistence.model.BuikPresent;
import com.assembly.common.persistence.model.GeneralPresent;
import com.assembly.common.persistence.model.Present;
import com.assembly.common.persistence.operation.BulkOperation;
import com.mongodb.BasicDBObject;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;

/**
 * @ClassName: BaseMongoService
 * @Description: mongodb持久层实现
 * @Description: 可扩展统一监控, 如:Prometheus
 * @author: k.y
 * @date: 2021年02月19日 3:54 下午
 */
@Slf4j
public class MServiceImpl<TDocument> extends MongoDBPersistence<TDocument> {

    @Override
    public BsonPresent findForCursor(BasicDBObject query) {
        FindIterable findIterable = getMongoTemplateByShardKey(null).getCollection(getDefaultCollectionName()).find(query);
        return new BsonPresent(findIterable, query, new AbstractOpCallback<Present, MongoCursor>() {
            @Override
            public MongoCursor call(Present fPresent) {
                BsonPresent present = (BsonPresent) fPresent;
                StopWatchExtend stopWatch = new StopWatchExtend();
                stopWatch.start("iterator(Present present)");
                MongoCursor<TDocument> dbCursor = present.getFindIterable().iterator();
                stopWatch.stop();
                if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
                    stopWatch.setId("collectionName:" + getDefaultCollectionName() + "|列表查询耗时" + "Query: " + present.toString());
                    log.info(stopWatch.prettyPrint());
                }
                return dbCursor;
            }
        });
    }

    @Override
    public int countDocuments(BasicDBObject query) {
        StopWatchExtend stopWatch = new StopWatchExtend();
        stopWatch.start("countDocuments(BasicDBObject query)");
        int count = (int) getMongoTemplateByShardKey(null).getCollection(getDefaultCollectionName()).countDocuments(query);
        stopWatch.stop();
        if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
            stopWatch.setId("collectionName:" + getDefaultCollectionName() + "|当前条件下列表信息统计总数耗时" + "Query: " + query.toString());
            log.info(stopWatch.prettyPrint());
        }
        return count;
    }

    @Override
    public BulkOperation registerBatch(BuikPresent buikPresent) {
        TartDB tartDB=this.getTartDB(buikPresent);
        BulkOperations bulkOperations=tartDB.getCurrentMongoTemplate().bulkOps(buikPresent.getBulkMode(), tartDB.getCurrentDocumentClass(), tartDB.getCurrentCollectionName());
        return new BulkOperation(bulkOperations, buikPresent, new AbstractOpCallback<BulkOperation, BulkWriteResult>() {
            @Override
            public BulkWriteResult call(BulkOperation bulkOperation) {
                StopWatchExtend stopWatch = new StopWatchExtend();
                stopWatch.start("iterator(QueryPresent present)");
                BulkWriteResult result=bulkOperation.getBulkOperations().execute();
                stopWatch.stop();
                if (stopWatch.getTotalTimeMillis() > Contants.TIME_MILLIS_CONSUMING) {
                    stopWatch.setId("collectionName:" + tartDB.getCurrentCollectionName() + "|列表查询耗时" + "Query: " + bulkOperation.toString());
                    log.info(stopWatch.prettyPrint());
                }
                return result;
            }
        });
    }

    @Override
    public BuikPresent bulkBuilder() {
        return new BuikPresent(this);
    }

    @Override
    public GeneralPresent mongodbBuilder() {
        return new GeneralPresent(this);
    }

}
