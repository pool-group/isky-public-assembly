package com.assembly.common.idwork;


/**
 * SportId 结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000- 0000000 - 00000000 - 000000000000000000
 * 1位标识
 *     由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 30位时间截(秒级)，
 *     30位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截秒级 - 开始时间截秒级)得到的值），
 *     这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
 * 7位的数据机器位
 *     相同服务最多可以部署在128个节点
 * 18位序列，
 *     秒内的计数，18位的计数顺序号支持每个节点每秒(同一机器，同一时间截)产生 262144 个ID序号
 * 8位分片位
 *     分库*分表 最多支持256 如订单id,分片位要和用户ID最后8位一致，方便同一用户分片到同一个库和表，同时支持 订单id查询和用户ID查询
 *
 * 加起来刚好64位，为一个Long型。
 * 优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据机器位ID做区分)，并且效率较高 。
 *
 *
 *
 * @author powell
 */
public class SportIdWorker {

    /** 开始时间截 (2020-01-01 00:00:00 秒) */
    private static final long TWEPOCH = 1577808000L;

    private static  final long TIMESTAMP_BITS = 30L;

    /** 机器id所占的位数 */
    public static  final long WORKER_ID_BITS = 7L;

    /** 支持的最大机器id，结果是127  */
    private static  final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /** 序列在id中占的位数 */
    private static  final long SEQUENCE_BITS = 18L;

    /** 分片位在id中占的位数*/
    private static  final long SHARD_ID_BITS = 8L;

    /** 生成分片的掩码 */
    private static  final long MAX_SHARD_ID_MASK = ~(-1L << SHARD_ID_BITS);


    /** 生成序列的掩码，这里为262144   */
    private static  final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /** 工作机器ID(0~127) */
    private long workerId;

    /** 毫秒内序列(0~262143) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     * @param workerId 工作ID (0~127)
     */
    public SportIdWorker(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.workerId = workerId;
    }



    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return
     */
    public synchronized long nextId(){
        return nextId(0);
    }

    /**
     * 从id获取分片ID
     *
     * @param id
     * @return
     */
    public static Long getShardId(long id){
        return id& MAX_SHARD_ID_MASK;
    }



    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId(long shardId) {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;
        long adjustShardId = shardId & MAX_SHARD_ID_MASK;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - TWEPOCH) << (SHARD_ID_BITS + SEQUENCE_BITS + WORKER_ID_BITS))
                | (workerId << (SHARD_ID_BITS + SEQUENCE_BITS))
                | (sequence << SHARD_ID_BITS)
                | adjustShardId;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis()/1000;
    }

    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        SportIdWorker idWorker = new SportIdWorker(0);
        int testStart = 20000000;
        int testEnd = 30000000;
        for (int i = testStart; i < testEnd; i++) {
            long id = idWorker.nextId(i);
            long shardId = SportIdWorker.getShardId(id);
            long shardIdTail = SportIdWorker.getShardId(i);

//            if(shardId-shardIdTail != 0) {
                System.out.println(shardId+"-"+shardIdTail+"-"+id);
//            }
        }
        System.out.println("cost:"+( System.currentTimeMillis()-start));
    }
}