import cn.tripman.util.FormatUtil;
import cn.tripman.util.JsonUtil;

public class FormatUtilTest {
    public static void main(String[] args) throws Exception {
        String json = "{\"id\":{\"name\":{\"name\":\"d\"}}}\n";
        Simplevo simplevo = FormatUtil.format(json, Simplevo.class);
        System.out.println(JsonUtil.toJSON(simplevo));
    }
}
