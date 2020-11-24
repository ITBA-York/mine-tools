package cn.tripman.helper;

import java.util.List;
import java.util.Map;

/**
 * @author liuyu
 */
public interface BaseCache {

    /**
     * 批量获取key
     */
    Map<String, String> get(List<String> keys);

    /**
     * 获取单个key
     */

    String get(String key);

    /**
     * 设置单个key的超时时间
     */
    void set(String key, String value, int expire);

    /**
     * 删除key
     */
    void delete(String key);

    /**
     * 是否加载缓存
     */
    boolean enable();
}
