package cn.tripman.bloom;

import redis.clients.jedis.Jedis;

public class RedisBloomFilterTest {
    private static final int NUM_APPROX_ELEMENTS = 60000 * 10000;
    private static final double FPP = 0.03;
    private static final int EXPIRE = 60 * 60 * 24 * 30;
    private static RedisBloomFilter redisBloomFilter;

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        redisBloomFilter = new RedisBloomFilter(NUM_APPROX_ELEMENTS, FPP, jedis);
        System.out.println("numHashFunctions: " + redisBloomFilter.getNumHashFunctions());
        System.out.println("bitmapLength: " + redisBloomFilter.getBitmapLength());
        String key = "comment";
        for (int i = 0; i < 1 * 1000 * 1000; i++) {
            redisBloomFilter.insert(key, i + "", EXPIRE);
        }
        //42067
        System.out.println(redisBloomFilter.mayExist(key, 422066 + ""));
        System.out.println(redisBloomFilter.mayExist(key, 43067 + ""));
        System.out.println(redisBloomFilter.mayExist(key, 42068 + ""));
        System.out.println(redisBloomFilter.mayExist(key, 42069 + ""));
    }


}