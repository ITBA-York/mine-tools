package cn.tripman.service;

import java.util.List;
import java.util.Map;

public interface CacheService {

    String getKey(String key);

    Map<String, String> mGets(List<String> keys);

    void set(String key, String value, int expire);

}
