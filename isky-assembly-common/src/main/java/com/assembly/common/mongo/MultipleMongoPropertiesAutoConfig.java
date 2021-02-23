package com.assembly.common.mongo;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author powell
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "mongodb.config")
@ConditionalOnProperty(name = "mongodb.config.collection-shard-size")
public class MultipleMongoPropertiesAutoConfig {

    private Integer collectionShardSize ;
    private MongoProperties cluster ;
}
