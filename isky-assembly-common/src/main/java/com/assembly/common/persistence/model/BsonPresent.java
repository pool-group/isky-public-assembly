package com.assembly.common.persistence.model;

import com.assembly.common.persistence.abstracts.AbstractOpCallback;
import com.assembly.common.util.BeanCopier;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BsonPresent
 * @Description: Bson类模型
 * @author: k.y
 * @date: 2021年02月21日 2:32 下午
 */
public class BsonPresent<TParam, TResult> extends Present<TParam,TResult> {

    private FindIterable findIterable;
    private Bson bsonQuery;
    private Bson sort;
    protected Integer limit;
    protected Integer current;

    public BsonPresent(FindIterable findIterable, Bson bsonQuery, AbstractOpCallback<TParam,TResult> function) {
        this.findIterable = findIterable;
        this.function = function;
        this.bsonQuery = bsonQuery;
    }

    public Present shardKey(Long shardKey) {
        this.shardKey = shardKey;
        return this;
    }

    public BsonPresent limit(int limit) {
        if (null != this.current) {
            this.findIterable.skip((this.current - 1) * limit);
        }
        this.limit = limit;
        this.findIterable.limit(limit);
        return this;
    }

    public BsonPresent current(int current) {
        if (null != limit) {
            this.findIterable.skip((current - 1) * this.limit);
        }
        this.current = current;
        return this;
    }

    public BsonPresent sort(Bson sort) {
        this.sort=sort;
        this.findIterable.sort(sort);
        return this;
    }

    public BsonPresent log(String log) {
        this.log = log;
        return this;
    }

    public FindIterable getFindIterable() {
        return this.findIterable;
    }

    public MongoCursor iterator() {
        return (MongoCursor) function.call((TParam) this);
    }

    public List<TParam> iterator(Class<TParam> returnClass) {
        this.entityClass = returnClass;
        MongoCursor<Map> dbCursor=iterator();
        List<TParam> resultList = Lists.newArrayList();
        if (null != dbCursor) {
            while (dbCursor.hasNext()) {
                TParam result = (TParam) BeanCopier.convertMapToClass(returnClass, dbCursor.next());
                resultList.add(result);
            }
        } else {
            return Lists.newArrayList();
        }
        return resultList;
    }

    @Override
    public String toString() {
        return "BsonPresent{" +
                "bsonQuery=" + bsonQuery +
                ", sort=" + sort +
                ", limit=" + limit +
                ", current=" + current +
                ", entityClass=" + entityClass +
                ", collectionName='" + collectionName + '\'' +
                ", shardKey=" + shardKey +
                ", log='" + log + '\'' +
                ", extend=" + extend +
                '}';
    }
}
