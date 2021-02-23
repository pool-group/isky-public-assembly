package com.assembly.common.persistence.model;

import com.assembly.common.persistence.operation.GeneralOperation;
import com.assembly.common.persistence.service.MService;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @ClassName: GeneralPresent
 * @Description: 常规类模型
 * @author: k.y
 * @date: 2021年02月21日 2:32 下午
 */
public class GeneralPresent<TParam,TResult> extends Present<TParam,TResult> {

    private Query query;

    public GeneralPresent(MService<TParam> mService){
        this.mService=mService;
    }

    public GeneralPresent(Query query, Class<TParam> returnClass) {
        this.query = query;
        this.entityClass = returnClass;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public GeneralPresent shardKey(Long shardKey) {
        this.shardKey = shardKey;
        return this;
    }

    public GeneralPresent log(String log) {
        this.log = log;
        return this;
    }

    public GeneralOperation build(){
        return new GeneralOperation(this, mService);
    }

    @Override
    public String toString() {
        return "GeneralPresent{" +
                "query=" + query +
                ", entityClass=" + entityClass +
                ", collectionName='" + collectionName + '\'' +
                ", shardKey=" + shardKey +
                ", log='" + log + '\'' +
                ", extend=" + extend +
                '}';
    }
}
