//package cn.tripman.util;
//
//import lombok.Data;
//
//import java.nio.charset.Charset;
//
//@Data
//public class RedisBloom {
//
//    //预计插入量
//    private long expectedInsertions;
//
//    //可接受的错误率
//    private double fpp;
//
//    private RedisTemplate redisTemplate;
//
//
//    //bit数组长度
//    private long numBits;
//    //hash函数数量
//    private int numHashFunctions;
//
//
//    public void init() {
//        this.numBits = optimalNumOfBits(expectedInsertions, fpp);
//        this.numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
//    }
//
//    //计算hash函数个数
//    private int optimalNumOfHashFunctions(long n, long m) {
//        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
//    }
//
//    //计算bit数组长度
//    private long optimalNumOfBits(long n, double p) {
//        if (p == 0) {
//            p = Double.MIN_VALUE;
//        }
//        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
//    }
//
//    /**
//     * 判断keys是否存在于集合
//     */
//    public boolean isExist(String key) {
//        long[] indexs = getIndexs(key);
//        List list = redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                redisConnection.openPipeline();
//                for (long index : indexs) {
//                    redisConnection.getBit("bf:hilite".getBytes(), index);
//                }
//                redisConnection.close();
//                return null;
//            }
//        });
//        return !list.contains(false);
//    }
//
//    /**
//     * 将key存入redis bitmap
//     */
//    public void put(String key) {
//        long[] indexs = getIndexs(key);
//        redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                redisConnection.openPipeline();
//                for (long index : indexs) {
//                    redisConnection.setBit("bf:hilite".getBytes(), index, true);
//                }
//                redisConnection.close();
//                return null;
//            }
//        });
//    }
//
//    /**
//     * 根据key获取bitmap下标
//     */
//    private long[] getIndexs(String key) {
//        long hash1 = hash(key);
//        long hash2 = hash1 >>> 16;
//        long[] result = new long[numHashFunctions];
//        for (int i = 0; i < numHashFunctions; i++) {
//            long combinedHash = hash1 + i * hash2;
//            if (combinedHash < 0) {
//                combinedHash = ~combinedHash;
//            }
//            result[i] = combinedHash % numBits;
//        }
//        return result;
//    }
//
//    /**
//     * 获取一个hash值
//     */
//    private long hash(String key) {
//        Charset charset = Charset.forName("UTF-8");
//        return Hashing.murmur3_128().hashObject(key, Funnels.stringFunnel(charset)).asLong();
//    }
//}
