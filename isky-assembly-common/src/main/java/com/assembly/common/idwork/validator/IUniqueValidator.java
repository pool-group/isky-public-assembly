package com.assembly.common.idwork.validator;

/**
 * serverId 唯一性校验接口
 * @author powell
 */
public interface IUniqueValidator {

    /**
     * 检测serverId是否合法
     *
     * @param serverId id生成器的server id
     * @param resourceId 启动服务资源唯一标示
     */
    void validator(Integer serverId,String resourceId);
}
