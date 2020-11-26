package cn.tripman.util;

import cn.tripman.constant.Constants;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static List<XSSFCell> toList(XSSFRow xssfRow) {
        TripArray<XSSFCell> cellArray = TripArray.newArray();
        if (xssfRow == null || xssfRow.getLastCellNum() < 1) {
            return cellArray;
        }
        for (int i = 1; i < xssfRow.getLastCellNum(); i++) {
            cellArray.add(xssfRow.getCell(i));
        }
        return cellArray;
    }
}
