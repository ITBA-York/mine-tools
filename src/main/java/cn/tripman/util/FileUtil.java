package cn.tripman.util;


import cn.tripman.constant.Constants;
import cn.tripman.helper.LineHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

/**
 * @author hero
 */
public class FileUtil {

    public static TripArray<String> readLine(File file) throws Exception {
        TripArray<String> result = TripArray.newArray();
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

    public static void printLine(File file) throws Exception {
        if (file == null || !file.exists()) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        LineHelper lineReader = LineHelper.getInstance();
        while (lineReader.readNext(reader.readLine())) {
            System.out.println(lineReader.getLine());
        }
    }

    public static String readLastLine(File file) throws Exception {
        String result = null;
        if (file == null || !file.exists()) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line = reader.readLine();
        while (line != null) {
            result = line;
            line = reader.readLine();
        }
        return result;
    }

    public static String readLastEmpty(File file) throws Exception {
        String result = null;
        int num = 0;
        if (file == null || !file.exists()) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line = reader.readLine();
        while (StringUtils.isNotEmpty(line)) {
            num++;
            result = line;
            line = reader.readLine();
        }
        System.out.println(num);
        return result;
    }

    public static void acceptLine(File file, Consumer<String> consumer, int thread) throws Exception {
        Semaphore semaphore = new Semaphore(thread);
        if (file == null || !file.exists()) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        LineHelper lineReader = LineHelper.getInstance();
        int num = 0;
        while (lineReader.readNext(reader.readLine())) {
            semaphore.acquire();
            if (num % 100 == 0) {
                System.out.println(reader.readLine());
            }
            num++;
            Constants.POOL.execute(() -> {
                consumer.accept(lineReader.getLine());
                semaphore.release();
            });
        }
    }

    public static void append(File file, String content) throws Exception {
        FileWriter writer = new FileWriter(file, true);
        writer.write(content);
        writer.close();
    }

}
