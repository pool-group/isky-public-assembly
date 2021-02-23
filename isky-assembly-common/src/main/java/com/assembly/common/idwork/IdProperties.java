package com.assembly.common.idwork;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * ID 生成器配置文件
 *
 * 要求相同应用，id唯一:
 *
 *
 * @author powell
 */
@Configuration
@ConditionalOnProperty(name = "sport.server.id")
@Data
public class IdProperties {
    /**
     * 相同服务的id需要唯一，0-127 之间
     */
    @Value(value = "${sport.server.id}")
    private Integer id;

    /**
     * 资源id
     *
     * 解决服务异常关闭,缓存未清理,导致启动失败
     */
    @Value(value = "${sport.server.resource-id}")
    private String resourceId;

}
