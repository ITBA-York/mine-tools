package cn.tripman.util;


import cn.tripman.constant.Constants;
import cn.tripman.helper.LineHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author hero
 */
public class FileUtil {

    public static List<String> readLine(File file) throws Exception {
        List<String> result = new LinkedList<>();
        if (file == null || !file.exists()) {
            return result;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        LineHelper lineReader = LineHelper.getInstance();
        while (lineReader.readNext(reader.readLine())) {
            result.add(lineReader.getLine());
        }
        return result;
    }

    public static void acceptLine(File file, Consumer<String> consumer, int thread) throws Exception {
        Semaphore semaphore = new Semaphore(thread);
        if (file == null || !file.exists()) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        LineHelper lineReader = LineHelper.getInstance();
        while (lineReader.readNext(reader.readLine())) {
            semaphore.acquire();
            Constants.POOL.execute(() -> consumer.accept(lineReader.getLine()));
        }
    }

    public static void append(File file, String content) throws Exception {
        FileWriter writer = new FileWriter(file, true);
        writer.write(content);
        writer.close();
    }

}
