package com.assembly.common.persistence.service;

import com.assembly.common.persistence.MongoDBPersistence;
import com.assembly.common.persistence.model.BsonPresent;
import com.assembly.common.persistence.model.BuikPresent;
import com.assembly.common.persistence.model.GeneralPresent;
import com.assembly.common.persistence.model.Present;
import com.assembly.common.persistence.operation.BulkOperation;
import com.mongodb.BasicDBObject;

/**
 * @ClassName: MongoService
 * @Description: Mongodb持久层接口
 * @author: k.y
 * @date: 2021年02月20日 2:18 下午
 */
public interface MService<TDocument> {

    /**
     * 获取当前DB
     *
     * @param present
     * @return
     */
    MongoDBPersistence.TartDB getTartDB(Present present);

    /**
     * 大批量数据查询, 推荐使用, 支持分库分表
     *
     * @param query
     * @return
     */
    BsonPresent findForCursor(BasicDBObject query);

    /**
     * 统计条件下的总数, 支持分库分表
     *
     * @param query
     * @return
     */
    int countDocuments(BasicDBObject query);

    /**
     * 批量处理注册
     *
     * @param present
     * @return
     */
    BulkOperation registerBatch(BuikPresent present);

    /**
     * bulk类构造器, 支持分库分表
     *
     * @return
     */
    BuikPresent bulkBuilder();

    /**
     * general类构造器, 支持分库分表
     *
     * @return
     */
    GeneralPresent mongodbBuilder();

}
