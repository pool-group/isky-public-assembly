package com.assembly.common.persistence.operation;

import com.assembly.common.persistence.abstracts.AbstractOpCallback;
import com.assembly.common.persistence.model.BuikPresent;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @ClassName: InsertPresent
 * @Description: BulkOperation API
 * @author: k.y
 * @date: 2021年02月21日 2:32 下午
 */
public class BulkOperation<TParam, TResult> implements BulkOperations {

    private BuikPresent buikPresent;
    private AbstractOpCallback<TParam, TResult> function;
    private BulkOperations bulkOperations;

    public BulkOperation(BulkOperations bulkOperations, BuikPresent buikPresent, AbstractOpCallback<TParam, TResult> function) {
        this.bulkOperations = bulkOperations;
        this.buikPresent = buikPresent;
        this.function = function;
    }

    public BulkOperations getBulkOperations() {
        return bulkOperations;
    }

    @Override
    public BulkOperations insert(List<? extends Object> list) {
        return bulkOperations.insert(list);
    }

    @Override
    public BulkOperations insert(Object o) {
        return bulkOperations.insert(o);
    }

    @Override
    public BulkOperations updateOne(Query query, Update update) {
        return bulkOperations.updateOne(query, update);
    }

    @Override
    public BulkOperations updateOne(List<Pair<Query, Update>> list) {
        return bulkOperations.updateOne(list);
    }

    @Override
    public BulkOperations updateMulti(Query query, Update update) {
        return bulkOperations.updateMulti(query, update);
    }

    @Override
    public BulkOperations updateMulti(List<Pair<Query, Update>> list) {
        return bulkOperations.updateMulti(list);
    }

    @Override
    public BulkOperations upsert(Query query, Update update) {
        return bulkOperations.upsert(query, update);
    }

    @Override
    public BulkOperations upsert(List<Pair<Query, Update>> list) {
        return bulkOperations.upsert(list);
    }

    @Override
    public BulkOperations remove(Query query) {
        return bulkOperations.remove(query);
    }

    @Override
    public BulkOperations remove(List<Query> list) {
        return bulkOperations.remove(list);
    }

    @Override
    public BulkWriteResult execute() {
        return (BulkWriteResult) function.call((TParam) this);
    }

    @Override
    public String toString() {
        return buikPresent.toString();
    }
}
