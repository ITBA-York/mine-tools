package cn.tripman.redis;

import cn.tripman.bloom.RedisPool;
import cn.tripman.aop.CacheInterface;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JedisClient implements CacheInterface {

    @Override
    public String getKey(String key) {
        try (Jedis jedis = RedisPool.getPool().getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public Map<String, String> mGets(List<String> keys) {
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isEmpty(keys)) {
            return map;
        }
        try (Jedis jedis = RedisPool.getPool().getResource()) {
            String[] strings = new String[keys.size()];
            List<String> results = jedis.mget(keys.toArray(strings));
            for (int i = 0; i < keys.size(); i++) {
                if (results.get(i) == null) {
                    continue;
                }
                map.put(keys.get(i), results.get(i));
            }
        }
        return map;
    }

    @Override
    public void set(String key, String value, int expire) {
        try (Jedis jedis = RedisPool.getPool().getResource()) {
            jedis.set(key, value, SetParams.setParams().nx().ex(expire));
        }
    }
}
