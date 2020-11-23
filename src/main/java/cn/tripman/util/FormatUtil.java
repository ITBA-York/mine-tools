package cn.tripman.util;

import cn.tripman.helper.TripJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hero
 */
public class FormatUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String split = "\\.";

    private static final List<String> empty = Arrays.asList("", "-");

    public static <T> T format(String json, Class<T> tClass) throws Exception {
        JsonNode jsonNode = mapper.readTree(json);
        return obj2Clazz(jsonNode, tClass);
    }


    /**
     * 当前node 转当前对象
     * 读取类型的字段列表
     * 把一个JSON对象转化成类型对象
     */
    private static <T> T obj2Clazz(JsonNode jsonNode, Class<T> tClass) throws Exception {
        if (jsonNode == null || jsonNode.isArray()) {
            return null;
        }
        Object base = JsonUtil.toBaseObj(tClass, jsonNode);
        if (base != null) {
            return (T) base;
        }
        //实例化类
        T t = tClass.newInstance();
        //字段返回类型列表
        Set<Field> fields = ClassUtil.fieldList(tClass)
                .stream()
                .filter(field -> field.getAnnotation(TripJson.class) != null)
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toSet());
        //List节点 获取list并赋值
        List<Field> arrays = fields.stream().filter(field -> field.getType() == List.class)
                .collect(Collectors.toList());
        for (Field field : arrays) {
            List<JsonNode> nodes = findByPathArray(field, jsonNode);
            convertArray(t, field, nodes);
        }
        //Object节点 获取对象并赋值
        List<Field> objects = fields.stream().filter(field -> field.getType() != List.class)
                .collect(Collectors.toList());
        for (Field field : objects) {
            JsonNode node = findByPath(field, jsonNode);
            setObject(t, field, node);
        }
        return t;
    }

    /**
     * Field属性不是list直接获取
     */
    public static JsonNode findByPath(Field field, JsonNode jsonNode) {
        String paths = field.getAnnotation(TripJson.class).value();
        if (empty.contains(paths)) {
            return jsonNode.get(field.getName());
        }
        for (String path : paths.split(split)) {
            jsonNode = jsonNode.get(path);
            if (jsonNode == null) {
                return null;
            }
        }
        return jsonNode;
    }


    /**
     * Field属性是list要考虑多个
     */
    public static List<JsonNode> findByPathArray(Field field, JsonNode jsonNode) {
        String paths = field.getAnnotation(TripJson.class).value();
        if (empty.contains(paths)) {
            return Arrays.asList(jsonNode.get(field.getName()));
        }
        return JsonUtil.childNodes(paths, jsonNode);
    }


    /**
     * 根节点处理List
     */
    public static <T> void convertArray(Object t, Field field, List<JsonNode> nodeList) {
        if (t == null || nodeList == null) {
            return;
        }
        try {
            Class<T> clazz = ClassUtil.fieldClass(field);
            List<T> list = new ArrayList<>();
            for (JsonNode array : nodeList) {
                list.add(obj2Clazz(array, clazz));
            }
            field.set(t, list);
        } catch (Exception ignored) {
        }
    }


    /**
     * 根节点处理对象
     */
    public static void setObject(Object t, Field field, JsonNode node) {
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
