package cn.tripman.util;


import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author hero
 */
public class DateUtil {

    private static final String MD_FORMAT = "MM-dd";
    private static final String YMD_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_MINUTES_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String TIME_FORMAT = "HH:mm:ss";


    private static String[] possibleDateFormats = new String[]{
            "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy.MM.dd", "yyyy年MM月dd日", "yyyy MM dd",
            "HH:mm:ss", "HH:mm",
            "yyyy-MM-dd HH:mm", "yyyy年MM月dd日 HH:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS+SS:SS",
            "yyyy-MM-dd'T'HH:mm:ssXXX"};

    public static Date toDate(String src) {
        Date outputDate = null;
        try {
            outputDate = DateUtils.parseDate(src, possibleDateFormats);
        } catch (ParseException var3) {
            var3.printStackTrace();
        }
        return outputDate;
    }


    public static String toDay(Date str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD_FORMAT);
        try {
            return simpleDateFormat.format(str);
        } catch (Exception e) {
        }
        return null;
    }


    public static String toDateTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        String date = null;
        try {
            return simpleDateFormat.format(time);
        } catch (Exception e) {
        }
        return null;
    }


    public static String toMonthDay(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MD_FORMAT);
        try {
            return simpleDateFormat.format(time);
        } catch (Exception e) {
        }
        return null;
    }


    public static String toDateMinutes(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_MINUTES_FORMAT);
        try {
            return simpleDateFormat.format(date);
        } catch (Exception e) {
        }
        return null;
    }


    public static String toTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
        try {
            return simpleDateFormat.format(date);
        } catch (Exception e) {
        }
        return null;
    }

    public static Date toDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.getTime();
    }

    public static Calendar toDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String stamp2Date(String str) {
        str = str.replace("/Date(", "").replace(")/", "");
        String time = str.substring(0, str.length() - 5);
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    public static boolean compareTwoTime(String time1, String time2) throws ParseException {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        //将字符串形式的时间转化为Date类型的时间
        Date a = sdf.parse(time1);
        Date b = sdf.parse(time2);
        //Date类的一个方法，如果a早于b返回true，否则返回false
        if (a.before(b)) {
            return true;
        } else {
            return false;
        }
    }


    public static String addDays(int days, String returnDateFormat) {
        Date date = DateUtils.addDays(new Date(), days);
        return DateFormatUtils.format(date, returnDateFormat);
    }

    /**
     * 给定日期是否在start，end内
     */
    public static boolean isInTimeRange(Date time, Date start, Date end) {
        if (time == null || start == null || end == null) {
            return false;
        }
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(start);

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(end);

        Calendar particularTime = Calendar.getInstance();
        particularTime.setTime(time);

        if (particularTime.after(beginTime) && particularTime.before(endTime)) {
            return true;
        } else {
            return false;
        }
    }


    public static final String getZoneTime(String format, Long offSet) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String date = dateFormat.format(new Date());
            Date bjDate = DateUtils.addSeconds(DateUtils.parseDate(date, format), offSet.intValue());
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(bjDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp toTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    public static int getSeaSon() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        switch (month) {
            case 2:
            case 3:
            case 4:
                return 1;
            case 5:
            case 6:
            case 7:
                return 2;
            case 8:
            case 9:
            case 10:
                return 3;
            case 11:
            case 0:
            case 1:
                return 4;
        }
        return 0;
    }

    public static int getSeaSon(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        switch (month) {
            case 2:
            case 3:
            case 4:
                return 1;
            case 5:
            case 6:
            case 7:
                return 2;
            case 8:
            case 9:
            case 10:
                return 3;
            case 11:
            case 0:
            case 1:
                return 4;
        }
        return 0;
    }
}
