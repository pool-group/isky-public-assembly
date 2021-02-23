package com.assembly.common.idwork;


import com.assembly.common.idwork.validator.IUniqueValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author powell
 */
@Component
@ConditionalOnProperty(name = "sport.server.id")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IdUtils  implements InitializingBean {

    private final IdProperties idProperties;

    private final IUniqueValidator uniqueValidator;

    private static SportIdWorker sportIdWorker;

    @Override
    public void afterPropertiesSet() {
        uniqueValidator.validator(idProperties.getId(),idProperties.getResourceId());
        sportIdWorker = new SportIdWorker(idProperties.getId());
    }

    /**
     * 获取id (无分片键)
     * @return id
     */
    public static Long nextId(){
        return SnowflakeIdWorker.generateId();
    }

    /**
     * 获取id
     * @param shardId  分片id 取二进制最后8位
     * @return id
     */
    public static long nextId(long shardId){
        return sportIdWorker.nextId(shardId);
    }

    /**
     * 从id获取分片键( 0-255)
     * @param id 生成后的id
     * @return shardId
     */
    public static long getShardId(long id){
        return SportIdWorker.getShardId(id);
    }


}

