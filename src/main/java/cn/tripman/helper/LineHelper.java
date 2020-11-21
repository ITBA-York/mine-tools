package cn.tripman.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hero
 */
@Data
@AllArgsConstructor
public class LineHelper {

    private String line;

    public static LineHelper getInstance() {
        return new LineHelper("");
    }

    public boolean readNext(String line) {
        if (line == null) {
            return false;
        }
        this.line = line;
        return true;
    }
}
