package cn.tripman.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlUtil {

    public static final String SPLIT_1 = "'";
    public static final String SPLIT_2 = "\"";
    public static final String SPLIT_3 = "\r";
    public static final String SPLIT_4 = "\n";

    public static final String CONVERTER = "\\";

    public static String toSqlString(String content) {
        if (content == null) {
            return null;
        }
        content = content.replace(SPLIT_3, "");
        content = Stream.of(content.split(SPLIT_4)).filter(Objects::nonNull).map(
                e -> {
                    e = e.replace(CONVERTER, CONVERTER + CONVERTER);
                    e = e.replace(SPLIT_1, CONVERTER + SPLIT_1);
                    e = e.replace(SPLIT_2, CONVERTER + SPLIT_2);
                    return e;
                }).collect(Collectors.joining(CONVERTER + "n"));
        return SPLIT_1 + content + SPLIT_1;
    }

    public static <T> Map<String, Object> getKey(Object tClass) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        Field[] fields = tClass.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(tClass));
        }
        return map;
    }

    public static String getSQL(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(StringUtils.join(map.keySet(), ","));
        builder.append(") ");
        builder.append("VALUES (");
        for (Object value : map.values()) {
            if (value == null) {
                builder.append("null,");
            } else {
                builder.append(toSqlString(value.toString())).append(",");
            }
        }
        return builder.substring(0, builder.length() - 1) + ");\n";
    }

    public static <T> String generateSQL(String start, List<T> list) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (T t : list) {
            Map<String, Object> map = getKey(t);
            builder.append(start);
            builder.append(getSQL(map));
        }
        return builder.toString();
    }
}
