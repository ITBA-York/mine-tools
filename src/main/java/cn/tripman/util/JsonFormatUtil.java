package cn.tripman.util;

import cn.tripman.helper.TripJson;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hero
 */
public class JsonFormatUtil {

    /**
     * 遍历节点
     */
    private <T> T format(JsonNode jsonNode, Class<T> tClass) throws Exception {
        T t = tClass.newInstance();
        for (Field field : ClassUtil.fieldList(tClass)) {
            TripJson trip = field.getAnnotation(TripJson.class);
            if (trip == null) {
                continue;
            }
            field.setAccessible(true);
            String path = trip.value();
            if ("-".equals(trip.value())) {
                path = field.getName();
            }
            List<T> list = convertField(jsonNode, path, ClassUtil.fieldClass(field), trip.tSerialize());
            if (field.getType() == List.class) {
                field.set(t, list);
            } else if (list.size() > 0) {
                field.set(t, list.get(0));
            }
        }
        return t;
    }

    /**
     * 当前节点类型判断
     */
    private <T> List<T> convert2Clazz(JsonNode jsonNode, Class<T> tClass, boolean tSerialize) {
        List<T> list = new ArrayList<>();
        if (jsonNode == null) {
            return list;
        }
        if (jsonNode.isArray()) {
            Optional.ofNullable(convert(jsonNode, tClass, tSerialize)).ifPresent(list::addAll);
        } else if (jsonNode.isObject()) {
            try {
                Optional.ofNullable(format(jsonNode, tClass)).ifPresent(list::add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Optional.ofNullable(convertObject(jsonNode, tClass, tSerialize)).ifPresent(list::add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public <T> List<T> convertField(JsonNode json, String path, Class<T> clazz, boolean tSerialize) {
        if (StringUtils.isEmpty(path)) {
            return convert2Clazz(json, clazz, tSerialize);
        }
        //获取path下的list
        List<T> results = new ArrayList<>();
        String[] nodes = path.split("\\.");
        List<JsonNode> tempList = new ArrayList<>(Collections.singletonList(json));
        List<JsonNode> newTemp = new ArrayList<>();
        List<JsonNode> newString = new ArrayList<>();
        for (int i = 0; i < nodes.length - 1; i++) {
            final int j = i;
            tempList.forEach(temp -> {
                if (temp.get(nodes[j]) == null) {
                    return;
                }
                if (temp.get(nodes[j]).isObject()) {
                    newTemp.add(temp.get(nodes[j]));
                } else if (temp.get(nodes[j]).isArray()) {
                    JsonNode jsonArray = temp.get(nodes[j]);
                    for (int k = 0; k < jsonArray.size(); k++) {
                        newTemp.add(jsonArray.get(k));
                    }
                } else if (temp.get(nodes[j]) != null) {
                    newString.add(temp.get(nodes[j]));
                }
            });
            tempList.clear();
            tempList.addAll(newTemp);
            newTemp.clear();
        }

        //对象强转
        if (!newString.isEmpty()) {
            return newString.stream().filter(Objects::nonNull).map(e -> convertObject(e, clazz, tSerialize)).collect(Collectors.toList());
        }
        //对象处理下一层节点 e.get(nodes[nodes.length - 1]
        tempList.stream()
                .map(e -> convert2Clazz(e.get(nodes[nodes.length - 1]), clazz, tSerialize))
                .forEach(results::addAll);
        return results;
    }

    public <T> List<T> convert(JsonNode array, Class<T> tClass, boolean tSerialize) {
        List<T> list = new ArrayList();
        if (array.size() == 0 && tClass == String.class) {
            return Collections.singletonList((T) "[]");
        }
        for (int i = 0; i < array.size(); i++) {
            list.add(convertObject(array.get(i), tClass, tSerialize));
        }
        return list;

    }

    /**
     * 处理对象
     */
    public <T> T convertObject(JsonNode obj, Class<T> tClass, boolean tSerialize) {
        Object result = null;
        try {
            if (obj == null) {
                return null;
            } else if (tClass == BigDecimal.class) {
                result = BigDecimal.valueOf(obj.asDouble());
            } else if (tClass == Integer.class || tClass == int.class) {
                result = Integer.parseInt(obj.toString());
            } else if (tClass == Long.class || tClass == long.class) {
                result = Long.parseLong(obj.toString());
            } else if (tClass == Float.class) {
                result = Float.parseFloat(obj.toString());
            } else if (tClass == Double.class) {
                result = Double.parseDouble(obj.toString());
            } else if (tClass == Date.class) {
                try {
                    if (obj.toString().length() > 20 && obj.toString().contains("/Date")) {
                        result = new Date(Long.parseLong(obj.toString().replace("/Date(", "").substring(0, 13)));
                    } else {
                        result = DateUtil.toDate(obj.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else if (tClass == String.class && obj.isObject()) {
                result = obj.asText();
            } else if (tClass == String.class && obj.isArray()) {
                result = obj.asText();
            } else if (obj.isObject() && tSerialize) {
                try {
                    return format(obj, tClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (obj.isObject()) {
                return JsonUtil.parse(obj.asText(), tClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(obj + tClass.toString());
        }
        return (T) result;
    }
}
