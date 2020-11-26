package cn.tripman.util;

import cn.tripman.constant.Constants;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hero
 */
public class ExcelUtil {

    public static String getNumberString(XSSFCell cell) {
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
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            Date date = HSSFDateUtil.getJavaDate(Double.valueOf(string));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
        return string;
    }

    public static Object getString(XSSFCell cell) {
        if (Constants.TRUE.equals(cell.getStringCellValue())) {
            return true;
        } else if (Constants.FALSE.equals(cell.getStringCellValue())) {
            return false;
        } else {
            return cell.getStringCellValue();
        }
    }

    public static TripArray<Map<String, Object>> readExcel(String excelFilePath) throws Exception {
        TripArray<Map<String, Object>> mapArrayList = TripArray.newArray();
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
                        map.put(key, ExcelUtil.getNumberString(cell));
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        map.put(key, ExcelUtil.getString(cell));
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

}
