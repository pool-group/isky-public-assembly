package com.assembly.common.persistence.model;

import com.assembly.common.persistence.abstracts.AbstractOpCallback;
import com.assembly.common.persistence.service.MService;

/**
 * @ClassName: Present
 * @Description: Present父类模型
 * @author: k.y
 * @date: 2021年02月21日 6:33 下午
 */
public abstract class Present<TParam,TResult> {

    protected MService<TParam> mService;
    protected AbstractOpCallback<TParam,TResult> function;
    protected Class<TParam> entityClass;
    protected String collectionName;
    /**分片键*/
    protected Long shardKey;
    /**自定义日志信息*/
    protected String log;
    /**扩展内容*/
    protected TParam extend;

    public Present(){
    }

    public Long getShardKey() {
        return shardKey;
    }

    public Class<TParam> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<TParam> entityClass) {
        this.entityClass = entityClass;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getLog() {
        return log;
    }

    public void setExtend(TParam extend) {
        this.extend = extend;
    }
}
