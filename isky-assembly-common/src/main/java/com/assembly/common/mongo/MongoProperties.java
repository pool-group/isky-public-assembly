package com.assembly.common.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author powell
 */

@Data
@NoArgsConstructor
public class MongoProperties {

    /**
     * 服务器地址
     */
    private List<String> address;

    /**
     * 副本集名称 单机可以不填写
     */
    private String replicaSet;

    /**
     * db
     */
    private String database;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 客户端最小连接数
     */
    private Integer minConnectionsPerHost;

    /**
     * 客户端最大连接数，超过了将会被阻塞，默认100
     */
    private Integer maxConnectionsPerHost;

    /**
     * 可被阻塞的线程数因子，默认值为5，如果connectionsPerHost配置为10，
     * 那么最多能阻塞50个线程，超过50个之后就会收到一个异常
     */
    private Integer threadsAllowedToBlockForConnectionMultiplier;

    /**
     * Sets the server selection timeout in milliseconds, which defines
     * how long the driver will wait for server selection to succeed
     * before throwing an exception.
     * <p>
     * 服务器查询超时时间，它定义驱动在抛出异常之前等待服务器查询成功，默认30s,单位milliseconds
     */
    private Integer serverSelectionTimeout;

    /**
     * 阻塞线程获取连接的最长等待时间，默认120000 ms
     */
    private Integer maxWaitTime;

    /**
     * 连接池连接最大空闲时间
     */
    private Integer maxConnectionIdleTime;

    /**
     * 连接池连接的最大存活时间
     */
    private Integer maxConnectionLifeTime;

    /**
     * 连接超时时间，默认值是0，就是不超时
     */
    private Integer connectTimeout;

    /**
     * socket超时时间，默认值是0，就是不超时
     */
    private Integer socketTimeout;

    /**
     * keep alive标志，默认false
     */
    private Boolean socketKeepAlive;

    /**
     * 驱动是否使用ssl进行连接，默认是false
     */
    private Boolean sslEnabled;

    /**
     * Define whether invalid host names should be allowed.
     * <p>
     * 定义是否允许使用无效的主机名
     */
    private Boolean sslInvalidHostNameAllowed;

    /**
     * Sets whether JMX beans registered by the driver should always be MBeans,
     * regardless of whether the VM is Java 6 or greater.
     */
    private Boolean alwaysUseMBeans;

    /**
     * Sets the connect timeout for connections used for the cluster heartbeat.
     * <p>
     * 为用于集群心跳的连接设置连接超时。
     */
    private Integer heartbeatConnectTimeout;

    /**
     * Sets the socket timeout for connections used for the cluster heartbeat.
     * <p>
     * 为用于集群心跳的连接设置套接字超时。
     */
    private Integer heartbeatSocketTimeout;

    /**
     * Sets the minimum heartbeat frequency.
     * <p>
     * 设置最小的心跳频率。
     */
    private Integer minHeartbeatFrequency;

    /**
     * Sets the heartbeat frequency.
     * <p>
     * 设置心跳频率
     */
    private Integer heartbeatFrequency;

    /**
     * Sets the local threshold.
     * <p>
     * 设置本地阈值。
     */
    private Integer localThreshold;

    /**
     * ssl 连接认证
     */
    private String authenticationDatabase;

}
