package cn.tripman.util;


import cn.tripman.constant.Constants;
import cn.tripman.helper.LineHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author hero
 */
public class FileUtil {

    public static List<Map<String, Object>> readExcel(String excelFilePath) throws Exception {
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        XSSFSheet xssfSheet = new XSSFWorkbook(new FileInputStream(excelFilePath)).getSheetAt(0);
        TripArray<String> mapKey = keys(xssfSheet);
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) {
                continue;
            }
            int cellNum = xssfRow.getLastCellNum();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < cellNum; i++) {
                String key = mapKey.get(i);
                XSSFCell cell = xssfRow.getCell(i);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        String string = cell.getNumericCellValue() + "";
                        if (string.contains("E")) {
                            DecimalFormat df = new DecimalFormat("0.00");
                            string = df.format(cell.getNumericCellValue());
                        }
                        if (string.endsWith(".00")) {
                            string = string.substring(0, string.length() - 3);
                        }
                        if (string.endsWith(".0")) {
                            string = string.substring(0, string.length() - 2);
                        }
                        map.put(key, string);
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(string));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            map.put(key, sdf.format(date));
                        }
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        if ("TRUE".equals(cell.getStringCellValue())) {
                            map.put(key, true);
                        } else if ("FALSE".equals(cell.getStringCellValue())) {
                            map.put(key, false);
                        } else {
                            map.put(key, cell.getStringCellValue());
                        }
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        map.put(key, cell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        map.put(key, cell.getCellFormula());
                        break;
                    default:
                        break;
                }
            }
            mapArrayList.add(map);
        }
        return mapArrayList;
    }

    private static TripArray<String> keys(XSSFSheet xssfSheet) {
        XSSFRow title = xssfSheet.getRow(0);
        TripArray<String> array = TripArray.newArray();
        for (int i = 0; i < title.getLastCellNum(); i++) {
            array.add(title.getCell(i).getStringCellValue());
        }
        return array;
    }

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
