package cn.tripman.service;

import java.util.Arrays;
import java.util.ServiceLoader;

public class CacheServiceClient {

    private static ServiceLoader<CacheService> clients = ServiceLoader.load(CacheService.class);

    public static CacheService getClient() {
        for (CacheService client : clients) {
            return client;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getClient().mGets(Arrays.asList("")));
    }
}
