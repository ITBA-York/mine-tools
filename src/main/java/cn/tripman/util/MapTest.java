package cn.tripman.util;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {
        OtherMap<Integer, Boolean> map = new OtherMap<>();
        map.put(1, true);
        System.out.println(map.getOrDefault(1, false));
    }
}
