package cn.tripman.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author hero
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJSON(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }

    public static <T> T parse(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    public static JsonNode readTree(String json) throws Exception {
        return mapper.readTree(json);
    }


}
