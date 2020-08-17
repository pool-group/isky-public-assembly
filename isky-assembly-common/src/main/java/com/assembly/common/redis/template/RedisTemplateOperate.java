package com.assembly.common.redis.template;

import com.assembly.common.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * com.assembly.common.redis.template
 *
 * @author duanpeng
 * @version Id: RedisTemplateOperate.java, v 0.1 2020年06月30日 10:53 duanpeng Exp $
 */
@Component
@RequiredArgsConstructor
public class RedisTemplateOperate {

    private final StringRedisTemplate stringRedisTemplate;
    protected final RedisTemplate redisTemplate;

    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, long time, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public void set(String key,String value,long time){
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    public void delete(String key){
        if(stringRedisTemplate.hasKey(key))
            stringRedisTemplate.delete(key);
    }

    public boolean hashPut(String var1,String var2,String var3){
        try {
            redisTemplate.opsForHash().put(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashPut is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
        return true;
    }

    public Object hashFind(String var1,String var2){
        try {
            return redisTemplate.opsForHash().get(var1,var2);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashFind is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
    }

    public List<Map<String,String>> hashValues(String var1){
        try {
            return redisTemplate.opsForHash().values(var1);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashValues is fail ! var1={0}",var1));
            throw e;
        }
    }

    public Cursor<Map.Entry<String, String>> hashScan(String var1){
        try {
            return redisTemplate.opsForHash().scan(var1, ScanOptions.NONE);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashScan is fail ! var1={0}",var1));
            throw e;
        }
    }

    public Long hashDelete(String var1,String var2){
        try {
            if(redisTemplate.opsForHash().hasKey(var1,var2)){
                return redisTemplate.opsForHash().delete(var1,var2);
            }
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("hashDelete is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
        return null;
    }

    public Long zSetDelete(String var1,String var2){
        try {
            if(null!=redisTemplate.opsForZSet().rank(var1,var2)){
                return redisTemplate.opsForZSet().remove(var1,var2);
            }
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetDelete is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
        return null;
    }

    public boolean zSetPut(String var1,String var2,double var3){
        try {
            redisTemplate.opsForZSet().add(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetPut is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
        return true;
    }

    public Set<String> zSetQuery(String var1, double var2, double var3){
        try {
            return redisTemplate.opsForZSet().rangeByScore(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetQuery is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
    }

    public double zSetIncrementScore(String var1,String var2,double var3){
        try {
            return redisTemplate.opsForZSet().incrementScore(var1,var2,var3);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("zSetIncrementScore is fail ! var1={0}, var2={1}, var3={2}",var1,var2,var3));
            throw e;
        }
    }

    public <T> Long listPutAll(String var1, List<T> var2){
        try {
            return redisTemplate.opsForList().rightPushAll(var1, var2);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("listPutAll is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
    }

    public <T> Long listPutOne(String var1, T var2){
        try {
            return redisTemplate.opsForList().rightPush(var1, var2);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("listPutOne is fail ! var1={0}, var2={1}",var1,var2));
            throw e;
        }
    }

    public <T> List<T> listGet(String var1){
        try {
            return redisTemplate.opsForList().range(var1, 0, -1);
        } catch (Exception e) {
            LogUtil.warn(MessageFormat.format("listGet is fail ! var1={0}",var1));
            throw e;
        }
    }

}
