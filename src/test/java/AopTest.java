import cn.tripman.aop.NativeAop;
import cn.tripman.util.FileUtil;

import java.io.File;

public class AopTest extends NativeAop {
    public static void main(String[] args) throws Exception {
        System.out.println(FileUtil.readLastEmpty(new File("F:\\id.csv")));
    }
}
