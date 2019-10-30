package com.apollo.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 全局启动类
 *
 * @author k.y
 * @version Id: com.zren.platform.test.Application.java, v 0.1 2018年11月05日 下午09:25 k.y Exp $
 */
@ComponentScan(basePackages = {"com.apollo.client"})
@SpringBootApplication
public class Application
{

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }
}
