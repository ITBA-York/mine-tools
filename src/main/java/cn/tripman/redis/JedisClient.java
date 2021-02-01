package cn.tripman.redis;

import cn.tripman.service.CacheService;

public class JedisClient implements CacheService {
    @Override
    public String getKey(String key) {
        return "jedis";
    }
}
