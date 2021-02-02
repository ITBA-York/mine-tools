package cn.tripman.bloom;

import io.rebloom.client.Client;

public class RedisBloomOffical {

    public static void main(String[] args) {
        Client client = new Client("localhost", 6379);
        client.add("simpleBloom", "Mark");
        // Does "Mark" now exist?
        client.exists("simpleBloom", "Mark"); // true
        client.exists("simpleBloom", "Farnsworth"); // False

    }
}
