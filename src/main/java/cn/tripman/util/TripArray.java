package cn.tripman.util;


import java.util.ArrayList;

/**
 * @author tripman
 */
public class TripArray<T> extends ArrayList<T> {

    public static <T> TripArray<T> newArray() {
        return new TripArray<>();
    }

    public String toJson() {
        return JsonUtil.toJSONDefaultNull(this);
    }

}
