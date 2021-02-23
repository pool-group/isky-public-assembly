package com.assembly.common.mongo;


import com.assembly.common.util.BeanCopier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;


/**
 * 配置类
 *
 * @author powell
 * @date 2020/04/20 12:15
 */

@Configuration
@ConditionalOnProperty(name = "spring.data.mongodb.database")
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoConfig extends AbstractMongoConfig{

    @Primary
    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return  new MongoTemplate(mongoDbFactory());
    }

    public static MongoConfig build(MongoProperties mongoProperties){
        MongoConfig mongoConfig = new MongoConfig();
        BeanCopier.copy(mongoProperties,mongoConfig);
        return mongoConfig;
    }

}