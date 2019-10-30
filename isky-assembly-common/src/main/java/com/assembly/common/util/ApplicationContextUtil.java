package com.assembly.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 上下文工具
 *
 * @author ken.ny
 * @version Id: ApplicationContextUtil.java, v 0.1 2018年11月22日 下午16:14 ken.ny Exp $
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {


    private static ApplicationContext APPLICATION_CONTEXT;


    public ApplicationContext getInstance() {
        return APPLICATION_CONTEXT;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.APPLICATION_CONTEXT = applicationContext;
    }
}
