import cn.tripman.helper.TripJson;
import lombok.Data;

@Data
public class Simplevo {

    @TripJson("id.name.name")
    private String name;

    @TripJson("id.name.id.test")
    private String id;
}
