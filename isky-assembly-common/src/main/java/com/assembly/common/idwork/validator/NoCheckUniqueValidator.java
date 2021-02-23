package com.assembly.common.idwork.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认不检测
 *
 * @author powell
 */
@Slf4j
@Component
public class NoCheckUniqueValidator implements IUniqueValidator {
    @Override
    public void validator(Integer serverId,String resourceId){
        log.warn("no check unique,sport.server.id:{}",serverId);
    }
}
