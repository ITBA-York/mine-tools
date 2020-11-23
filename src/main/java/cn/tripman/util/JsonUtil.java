package cn.tripman.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hero
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String DOT = "\\.";


    public static String toJSON(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }

    public static <T> T parse(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    public static JsonNode readTree(String json) throws Exception {
        return mapper.readTree(json);
    }

    /**
     * 数组转化
     */
    private static List<JsonNode> nodes(JsonNode jsonNode) {
        List<JsonNode> result = new ArrayList<>();
        if (jsonNode == null) {
            return result;
        }
        if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                result.add(jsonNode.get(i));
            }
            return result;
        }
        result.add(jsonNode);
        return result;
    }

    /**
     * 节点下的对象，考虑当前节点不为数组,忽略空数据
     */
    private static List<JsonNode> getChild(String path, List<JsonNode> nodes) {
        List<JsonNode> result = new ArrayList<>();
        nodes.stream().map(e -> nodes(e.get(path))).forEach(result::addAll);
        return result;
    }

    /**
     * 取子节点内容
     */
    public static List<JsonNode> childNodes(String paths, JsonNode node) {
        return childNodes(Arrays.asList(paths.split(DOT)), nodes(node));
    }

    private static List<JsonNode> childNodes(List<String> paths, List<JsonNode> nodes) {
        List<JsonNode> result = new ArrayList<>();
        if (paths == null || paths.size() == 0 || nodes == null || nodes.size() == 0) {
            return result;
        }
        if (paths.size() == 1) {
            nodes.stream().map(e -> getChild(paths.get(0), Collections.singletonList(e))).forEach(result::addAll);
            return result;
        }
        result.addAll(childNodes(paths.subList(1, paths.size()), getChild(paths.get(0), nodes)));
        return result;
    }

    public static <T> Object toBaseObj(Class<T> clazz, JsonNode node) {
        if (clazz == null || node == null) {
            return null;
        }
        if (clazz == Boolean.class || clazz == boolean.class) {
            return node.asBoolean();
        }
        if (clazz == Short.class || clazz == short.class) {
            return node.shortValue();
        }
        if (clazz == Integer.class || clazz == int.class) {
            return node.intValue();
        }
        if (clazz == Long.class || clazz == long.class) {
            return node.longValue();
        }
        if (clazz == Float.class || clazz == float.class) {
            return node.floatValue();
        }
        if (clazz == Double.class || clazz == double.class) {
            return node.doubleValue();
        }
        if (clazz == BigDecimal.class) {
            return node.decimalValue();
        }
        if (clazz == String.class) {
            return node.textValue();
        }
        return null;
    }

}
