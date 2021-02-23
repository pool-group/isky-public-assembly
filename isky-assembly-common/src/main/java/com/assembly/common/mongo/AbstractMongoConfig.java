package com.assembly.common.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author powell
 */
@Slf4j
public abstract class AbstractMongoConfig  extends MongoProperties {

    /**
     * 获取 MongoTemplate
     * @return
     * @throws Exception
     */
    public abstract MongoTemplate mongoTemplate() throws Exception;

    public MongoDbFactory mongoDbFactory() throws Exception {

        MongoClientOptions mongoClientOptions = initMongoClientOptions();

        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String address : this.getAddress()) {
            String[] hostAndPort = address.split(":");
            String host = hostAndPort[0];
            Integer port = Integer.parseInt(hostAndPort[1]);
            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }

        log.info("serverAddresses:" + serverAddresses.toString());


        MongoClient mongoClient ;
        if (!StringUtils.isEmpty(this.getUsername())) {
            // 连接认证
            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(
                    this.getUsername(), this.getAuthenticationDatabase() != null
                            ? this.getAuthenticationDatabase() : this.getDatabase(),
                    this.getPassword().toCharArray());

            // 创建认证客户端
            mongoClient = new MongoClient(serverAddresses, mongoCredential, mongoClientOptions);
        } else {
            // 创建非认证客户端
            mongoClient = new MongoClient(serverAddresses, mongoClientOptions);
        }

        // 创建MongoDbFactory
        return new SimpleMongoDbFactory(mongoClient, this.getDatabase());
    }

    /**
     * 初始化 MongoClientOptions
     * @return
     */
    private MongoClientOptions initMongoClientOptions(){
        // 客户端配置（连接数，副本集群验证）
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

        Integer intValue = this.getMaxConnectionsPerHost();
        if (intValue != null) {
            builder.connectionsPerHost(intValue);
        }

        intValue = this.getMinConnectionsPerHost();
        if (intValue != null && intValue > 0) {
            builder.minConnectionsPerHost(intValue);
        }

        String strValue = this.getReplicaSet();
        if (!StringUtils.isEmpty(strValue)) {
            builder.requiredReplicaSetName(strValue);
        }

        intValue = this.getThreadsAllowedToBlockForConnectionMultiplier();
        if (intValue != null) {
            builder.threadsAllowedToBlockForConnectionMultiplier(intValue);
        }

        intValue = this.getServerSelectionTimeout();
        if (intValue != null) {
            builder.serverSelectionTimeout(intValue);
        }

        intValue = this.getMaxWaitTime();
        if (intValue != null) {
            builder.maxWaitTime(intValue);
        }

        intValue = this.getMaxConnectionIdleTime();
        if (intValue != null) {
            builder.maxConnectionIdleTime(intValue);
        }

        intValue = this.getMaxConnectionLifeTime();
        if (intValue != null) {
            builder.maxConnectionLifeTime(intValue);
        }

        intValue = this.getConnectTimeout();
        if (intValue != null) {
            builder.connectTimeout(intValue);
        }

        intValue = this.getSocketTimeout();
        if (intValue != null) {
            builder.socketTimeout(intValue);
        }

        Boolean booleanValue = this.getSslEnabled();
        if (booleanValue != null) {
            builder.sslEnabled(booleanValue);
        }

        booleanValue = this.getSslInvalidHostNameAllowed();
        if (booleanValue != null) {
            builder.sslInvalidHostNameAllowed(booleanValue);
        }

        booleanValue = this.getAlwaysUseMBeans();
        if (booleanValue != null) {
            builder.alwaysUseMBeans(booleanValue);
        }

        intValue = this.getHeartbeatFrequency();
        if (intValue != null) {
            builder.heartbeatFrequency(intValue);
        }

        intValue = this.getMinHeartbeatFrequency();
        if (intValue != null) {
            builder.minHeartbeatFrequency(intValue);
        }

        intValue = this.getHeartbeatConnectTimeout();
        if (intValue != null) {
            builder.heartbeatConnectTimeout(intValue);
        }

        intValue = this.getHeartbeatSocketTimeout();
        if (intValue != null) {
            builder.heartbeatSocketTimeout(intValue);
        }


        intValue = this.getLocalThreshold();
        if (intValue != null) {
            builder.localThreshold(intValue);
        }

        return builder.build();
    }
}
