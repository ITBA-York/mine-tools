package cn.tripman.aop;

import java.util.ServiceLoader;

public class CacheClient {

    private static ServiceLoader<CacheInterface> clients = ServiceLoader.load(CacheInterface.class);

    public static CacheInterface getClient() {
        for (CacheInterface client : clients) {
            return client;
        }
        return null;
    }
}
