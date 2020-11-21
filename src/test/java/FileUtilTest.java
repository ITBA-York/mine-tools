import cn.tripman.util.FileUtil;

import java.io.File;

public class FileUtilTest {
    public static void main(String[] args) {
        FileUtil.readLine(new File("Z:\\test.txt")).forEach(System.out::println);
    }
}
