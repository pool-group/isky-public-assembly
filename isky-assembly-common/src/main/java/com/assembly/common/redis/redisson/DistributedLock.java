package com.assembly.common.redis.redisson;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 *  redis分布式锁
 *
 * @author ken.ny
 * @version Id: BLock.java, v 0.1 2019年01月30日 下午11:40 ken.ny Exp $
 */
@Component
public class DistributedLock {

    @Autowired
    private RedissonClient redisClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public HLock getLock(String lockName){
        return new HLock(lockName,redisClient,stringRedisTemplate);
    }

}
