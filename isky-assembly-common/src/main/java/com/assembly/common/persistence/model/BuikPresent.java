package com.assembly.common.persistence.model;

import com.assembly.common.persistence.operation.BulkOperation;
import com.assembly.common.persistence.service.MService;
import org.springframework.data.mongodb.core.BulkOperations;

/**
 * @ClassName: GeneralPresent
 * @Description: Buik类模型
 * @author: k.y
 * @date: 2021年02月21日 2:32 下午
 */
public class BuikPresent<TParam,TResult> extends Present<TParam,TResult> {

    protected BulkOperations.BulkMode bulkMode=BulkOperations.BulkMode.UNORDERED;

    public BuikPresent(MService<TParam> mService){
        this.mService=mService;
    }

    public BuikPresent shardKey(Long shardKey) {
        this.shardKey = shardKey;
        return this;
    }

    public BuikPresent bulkMode(BulkOperations.BulkMode bulkMode) {
        this.bulkMode = bulkMode;
        return this;
    }

    public BuikPresent log(String log) {
        this.log = log;
        return this;
    }

    public BuikPresent entityClass(Class<TParam> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public BulkOperations.BulkMode getBulkMode() {
        return bulkMode;
    }

    /**
     * 默认BulkMode:UNORDERED
     *
     * @return
     */
    public BulkOperation registerBatch() {
        return mService.registerBatch(this);
    };

    @Override
    public String toString() {
        return "BuikPresent{" +
                "bulkMode=" + bulkMode +
                ", entityClass=" + entityClass +
                ", collectionName='" + collectionName + '\'' +
                ", shardKey=" + shardKey +
                ", log='" + log + '\'' +
                ", extend=" + extend +
                '}';
    }
}
