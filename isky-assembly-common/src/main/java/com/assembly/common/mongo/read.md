
1.MultipleMongoTemplate分库分表配置方式:

mongodb:
  config:
    collection-shard-size: 3
    cluster:
      address: test-mongo.ceusszzollnk.ap-south-1.docdb.amazonaws.com:27017,test-mongo.ceusszzollnk.ap-south-1.docdb.amazonaws.com:27017,test-mongo.ceusszzollnk.ap-south-1.docdb.amazonaws.com:27017
      #    replica-set:
      database: message
      username: root
      password: root123456
      min-connections-per-host: 10
      max-connections-per-host: 100
      threads-allowed-to-block-for-connection-multiplier: 5
      server-selection-timeout: 30000
      max-wait-time: 120000
      max-connection-idel-time: 0
      max-connection-life-time: 0
      connect-timeout: 10000
      socket-timeout: 0
      socket-keep-alive: false
      ssl-enabled: false
      ssl-invalid-host-name-allowed: false
      always-use-m-beans: false
      heartbeat-socket-timeout: 20000
      heartbeat-connect-timeout: 20000
      min-heartbeat-frequency: 500
      heartbeat-frequency: 10000
      local-threshold: 15
