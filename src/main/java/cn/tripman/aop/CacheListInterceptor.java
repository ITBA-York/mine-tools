package cn.tripman.aop;

import cn.tripman.service.CacheService;
import cn.tripman.service.CacheServiceClient;
import cn.tripman.util.ClassUtil;
import cn.tripman.util.GsonUtil;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
public class CacheListInterceptor<E> {

    private final CacheService client = CacheServiceClient.getClient();

    /**
     * 参数列表举例1：
     * 参数是：1    's'    [1,2,3]
     * 缓存建：1_s_1 1_s_2 1_s_3
     * 缓存值：数组
     * 每个建对应多个value，是1对多关系，没有就是空数组
     * <p>
     * 取结果的时候也是直接去取，没有的就是空
     */

    @Around("@annotation(cn.tripman.aop.CacheList)")
    public Object execute(ProceedingJoinPoint point) throws Throwable {
        //切入方法
        Method method = ClassUtil.getMethod(point);
        //返回结果
        Class<E> clazz = ClassUtil.getReturnClass(method);
        //注解
        CacheList cacheList = method.getAnnotation(CacheList.class);
        //映射关系
        Method getMethod = StringUtils.isEmpty(cacheList.method()) ? null : clazz.getDeclaredMethod(cacheList.method());
        //请求参数
        Object[] args = point.getArgs();
        //list节点
        int index = ClassUtil.getListIndex(args);
        //java和redis key的关系
        Map<Object, String> keyMap = ClassUtil.joinKeys(cacheList.key() + "1", point.getArgs());
        //redis key value 关系
        Map<String, String> redisMap = client.mGets(new ArrayList<>(keyMap.values()));
        //redis没有的数据
        List<Object> missIds = keyMap.keySet().stream()
                .filter(e -> !redisMap.containsKey(keyMap.get(e)))
                .collect(Collectors.toList());
        if (index == -1) {
            String key = keyMap.values().stream().findFirst().get();
            if (CollectionUtils.isNotEmpty(missIds)) {
                Object obj = point.proceed();
                if (obj != null) {
                    client.set(key, GsonUtil.toJson(obj), cacheList.expire());
                } else {
                    client.set(key, method.getReturnType() == List.class ? "[]" : "{}", cacheList.expire());
                }
                return obj;
            }
            return method.getReturnType() == List.class
                    ? GsonUtil.fromList(redisMap.values().stream().findFirst().get(), clazz)
                    : GsonUtil.fromJson(redisMap.values().stream().findFirst().get(), clazz);
        }
        if (CollectionUtils.isNotEmpty(missIds)) {
            args[index] = missIds;
            Object newResult = point.proceed(args);
            if (newResult != null) {
                //参数id有返回结果的处理
                ArrayListMultimap<Object, Object> multimap = ArrayListMultimap.create();
                for (Object obj : (List) newResult) {
                    Object newId = getMethod.invoke(obj);
                    multimap.put(newId, obj);
                }
                multimap.keySet().forEach(e -> {
                    String value = GsonUtil.toJson(multimap.get(e));
                    client.set(keyMap.get(e), value, cacheList.expire());
                    redisMap.put(keyMap.get(e), value);
                });
            }
            //参数id没有返回结果记录空数组
            keyMap.values().stream()
                    .filter(e -> !redisMap.containsKey(e))
                    .forEach(e -> client.set(e, "[]", cacheList.expire()));
        }
        List<Object> result = new ArrayList<>();
        //构建结果集合
        keyMap.values().forEach(e -> result.addAll(GsonUtil.fromList(redisMap.get(e), clazz)));
        return result;
    }

}
