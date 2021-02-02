package cn.tripman.util;

import java.util.HashMap;

public class OtherMap<K, V> extends HashMap<K, V> {

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }
}
