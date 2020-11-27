package cn.tripman.util;


import cn.tripman.constant.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public <T> TripArray<TripArray<T>> splitList(List<T> list, int num) {
        TripArray<TripArray<T>> result = TripArray.newArray();
        if (list == null) {
            return result;
        }
        TripArray<T> temp = TripArray.newArray();
        for (T t : list) {
            temp.add(t);
            if (temp.size() == num) {
                result.add(temp);
                temp = TripArray.newArray();
            }
        }
        if (temp.size() > 0) {
            result.add(temp);
        }
        return result;
    }

    public void accepts(Consumer<TripArray<T>> consumer, int thread, int size) throws Exception {
        Semaphore semaphore = new Semaphore(thread);
        List<TripArray<T>> lists = splitList(this, size);
        for (TripArray<T> list : lists) {
            semaphore.acquire();
            Constants.POOL.execute(() -> {
                consumer.accept(list);
                semaphore.release();
            });
        }
    }

    public void accept(Consumer<T> consumer, int thread) throws Exception {
        Semaphore semaphore = new Semaphore(thread);
        for (T t : this) {
            semaphore.acquire();
            Constants.POOL.execute(() -> {
                consumer.accept(t);
                semaphore.release();
            });
        }
    }

    public <R> Map<String, T> toMap(Function<T, R> function) {
        Map<String, T> map = new HashMap<>();
        this.forEach(e -> map.put(function.apply(e).toString(), e));
        return map;
    }

}
