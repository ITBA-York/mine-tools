package cn.tripman.bloom;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    public static JedisPool getPool() {
        //创建redis连接池配置对象
        JedisPoolConfig jpc = new JedisPoolConfig();
        //最大连接空闲数
        jpc.setMaxIdle(20);
        //最大连接数
        jpc.setMaxTotal(50);
        //创建连接池对象
        JedisPool jp = new JedisPool(jpc, "127.0.0.1", 6379);
        return jp;
    }

}
