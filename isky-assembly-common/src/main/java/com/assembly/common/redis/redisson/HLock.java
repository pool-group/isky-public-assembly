package com.assembly.common.redis.redisson;

import com.assembly.common.util.AssertUtil;
import com.assembly.common.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现
 *
 * @author ken.ny
 * @version Id: HLock.java, v 0.1 2019年01月30日 下午16:09 ken.ny Exp $
 */
public class HLock implements AutoCloseable {

    private RedissonClient redisClient;

    private StringRedisTemplate stringRedisTemplate;

    private RLock lock=null;

    private String lockName;

    public HLock(String lockName,RedissonClient redisClient,StringRedisTemplate stringRedisTemplate) {
        this.lockName = lockName;
        this.redisClient=redisClient;
        this.stringRedisTemplate=stringRedisTemplate;
    }

    public boolean lock(){
        AssertUtil.isTrue(StringUtils.isBlank(this.lockName),"this lockName is null, please checking");
        lock=redisClient.getLock(this.lockName);
        lock.lock(10, TimeUnit.SECONDS);
        if (lock.isLocked())
            LogUtil.info(String.format(" 获取redis lock锁成功：[%s]",lock.isLocked()));
        return lock.isLocked();
    }

    public boolean lock(long var1,TimeUnit var2){
        AssertUtil.isTrue(StringUtils.isBlank(this.lockName),"this lockName is null, please checking");
        lock=redisClient.getLock(this.lockName);
        lock.lock(var1, var2);
        if (lock.isLocked())
            LogUtil.info(String.format(" 获取redis lock锁成功：[%s]",lock.isLocked()));
        return lock.isLocked();
    }

    public boolean tryLock(){
        AssertUtil.isTrue(StringUtils.isBlank(this.lockName),"this lockName is null, please checking");
        lock=redisClient.getLock(this.lockName);
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(0, 10, TimeUnit.SECONDS);
            if (isLocked) {
                LogUtil.info(String.format(" 获取redis tryLock锁成功：[%s]",isLocked));
                return true;
            }
        } catch (InterruptedException e) {
            LogUtil.info(String.format(" 获取redis tryLock锁失败：[%s]",isLocked));
            return false;
        }
        LogUtil.info(String.format(" 获取redis tryLock锁失败：[%s]",isLocked));
        return false;
    }

    public boolean tryLock(long var1,long var2,TimeUnit var3){
        AssertUtil.isTrue(StringUtils.isBlank(this.lockName),"this lockName is null, please checking");
        lock=redisClient.getLock(this.lockName);
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(var1, var2, var3);
            if (isLocked) {
                LogUtil.info(String.format(" 获取redis tryLock锁成功：[%s]",isLocked));
                stringRedisTemplate.opsForValue().set("lock:"+this.lockName+"executeTime.lock", String.valueOf(System.currentTimeMillis() + 30 * 1000));
                return true;
            }
        } catch (InterruptedException e) {
            LogUtil.info(String.format(" 获取redis tryLock锁失败：[%s]",isLocked));
            return false;
        }
        LogUtil.info(String.format(" 获取redis tryLock锁失败：[%s]",isLocked));
        return false;
    }

    /**
     * 释放分布式锁
     */
    public void unlock(){
        if(null!=lock&&lock.isLocked())
            lock.unlock();
    }

    /**
     * AutoCloseable
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if(null!=lock&&lock.isLocked()){
            lock.unlock();
            LogUtil.info(" lock.unlock is success!");
        }
    }
}
