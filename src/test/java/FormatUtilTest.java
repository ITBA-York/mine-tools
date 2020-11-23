import cn.tripman.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;


public class FormatUtilTest {
    public static void main(String[] args) throws Exception {
        JsonNode tree = JsonUtil.readTree("[{\"id\":{\"name\":{\"name\":[\"d\"]}}},{\"id\":{\"name\":{\"name\":[\"d\"]}}}\n]");
        System.out.println(JsonUtil.childNodes("id.name.name", tree));

    }
}
