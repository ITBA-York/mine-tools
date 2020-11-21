package cn.tripman.util;

import cn.tripman.helper.TripJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hero
 */
public class FormatUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T format(String json, Class<T> tClass) throws Exception {
        JsonNode jsonNode = mapper.readTree(json);
        return obj2Clazz(jsonNode, tClass);
    }


    /**
     * 当前node 转当前对象
     * 读取类型的字段列表
     * 把一个JSON对象转化成类型对象
     */
    private static <T> T obj2Clazz(JsonNode jsonNode, Class<T> tClass) {
        try {
            //实例化类
            T t = tClass.newInstance();
            //字段返回类型列表
            Set<Field> fields = ClassUtil.fieldList(tClass)
                    .stream()
                    .filter(field -> field.getAnnotation(TripJson.class) != null)
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toSet());
            //List节点
            fields.stream().filter(field -> field.getType() == List.class)
                    .forEach(field -> convertArray(t, field, findByPath(field.getAnnotation(TripJson.class).value(), jsonNode)));
            //Object节点
            fields.stream().filter(field -> field.getType() != List.class)
                    .forEach(field -> convertObject(t, field, findByPath(field.getAnnotation(TripJson.class).value(), jsonNode)));
            return t;
        } catch (Exception ignored) {
        }
        return null;
    }

    public static JsonNode findByPath(String paths, JsonNode jsonNode) {
        if (paths == null || "".equals(paths) || "-".equals(paths)) {
            return jsonNode;
        }
        for (String path : paths.split("\\.")) {
            jsonNode = jsonNode.get(path);
            if (jsonNode == null) {
                return null;
            }
        }
        return jsonNode;
    }


    /**
     * 根节点处理List
     */
    public static void convertArray(Object t, Field field, JsonNode array) {
        if (t == null || array == null) {
            return;
        }
        try {
            Class clazz = ClassUtil.fieldClass(field);
            List list = new ArrayList();
            Iterator<JsonNode> iterator = array.elements();
            while (iterator.hasNext()) {
                JsonNode jsonNode = iterator.next();
                list.add(obj2Clazz(jsonNode, clazz));
            }
            field.set(t, list);
        } catch (Exception ignored) {
        }
    }


    /**
     * 根节点处理对象
     */
    public static void convertObject(Object t, Field field, JsonNode node) {
        if (t == null || field == null || node == null) {
            return;
        }
        try {
            Class clazz = field.getType();
            if (clazz == Integer.class || clazz == int.class) {
                field.set(t, node.intValue());
            }
            if (clazz == Long.class || clazz == long.class) {
                field.set(t, node.longValue());
            }
            if (clazz == Float.class || clazz == float.class) {
                field.set(t, node.floatValue());
            }
            if (clazz == Double.class || clazz == double.class) {
                field.set(t, node.doubleValue());
            }
            if (clazz == BigDecimal.class) {
                field.set(t, node.decimalValue());
            }
            if (clazz == String.class) {
                field.set(t, node.textValue());
            }
            if (node.isObject()) {
                field.set(t, obj2Clazz(node, field.getType()));
            }
        } catch (Exception ignored) {
        }
    }
}
