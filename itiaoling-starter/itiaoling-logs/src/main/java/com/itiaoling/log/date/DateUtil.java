package com.itiaoling.log.date;


import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/************************************************************
 * Copy Right Information :
 * Project :
 * JDK version used : 1.8.0
 *
 * Comments :
 *       时间工具类 基于1.8版本 新API 方法
 * Modification history :
 *
 * Sr Date         Modified By     Why & What is modified
 * 1. 2023/05/24  charles         elk timestamp format as yyyy-MM-dd'T'HH:mm:ss.SSS'Z' UTF
 ***********************************************************/
public final class DateUtil {

    private DateUtil() {
    }

    /**
     * 设置为伦敦时区
     */
    public static void setTimeZone(ZoneId zoneId) {
        if (zoneId == null) {
            return;
        }
        if (!TimeZone.getDefault().toZoneId().equals(zoneId)) {
            TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
        }
    }

    ////////////////////////////////////////////////////////////////////////
    /////////////////////////  获取当前时间 //////////////////////////////////
    public static LocalDateTime getNowOfLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate getNowOfLocalDate() {
        return LocalDate.now();
    }

    public static Date getNowOfDate() {
        return new Date();
    }

    public static String getNowOfString() {
        return convertToString(LocalDateTime.now(), DateTimeFormatEnum.yyyyMMddHHmmss);
    }

    public static String getNowOfString(DateFormatEnum dateFormat) {
        return convertToString(new Date(), dateFormat);
    }

    public static String getNowOfString(TimeFormatEnum timeFormat) {
        return convertToString(new Date(), timeFormat);
    }

    public static String getNowOfString(DateTimeFormatEnum dateTimeFormat) {
        return convertToString(new Date(), dateTimeFormat);
    }

    /**
     * 获取当前时间的ISO8601格式 UTC时间
     *
     * @return 2018-04-24T08:00:00.000Z
     */
    public static String getNowOfISO8601String() {
        SimpleDateFormat formatter = new SimpleDateFormat(DateTimeFormatEnum.ISO8601_YYYY_MM_DD_T_HH_mm_ss_SSS_Z.getFormat());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(System.currentTimeMillis());
    }

    public static LocalDate getTommorrow() {
        return LocalDate.now().plusDays(1L);
    }
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    /////////////////////////  日期转字符串 //////////////////////////////////
    public static String convertToString(LocalDateTime date) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(DateTimeFormatEnum.YYYY_MM_DDHHmmss.getFormat());
        return date.format(var);
    }

    public static String convertToString(LocalDateTime date, DateTimeFormatEnum format) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(format.getFormat());
        return date.format(var);
    }

    public static String convertToString(LocalTime date) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(TimeFormatEnum.HH_mm_ss.getFormat());
        return date.format(var);
    }

    public static String convertToString(LocalTime date, TimeFormatEnum format) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(format.getFormat());
        return date.format(var);
    }

    public static String convertToString(LocalDate date) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM_DD.getFormat());
        return date.format(var);
    }

    public static String convertToString(LocalDate date, DateFormatEnum format) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(format.getFormat());
        return date.format(var);
    }

    public static String convertToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateTimeFormatEnum.YYYY_MM_DDHHmmss.getFormat());
        return formatter.format(date);
    }

    public static String convertToString(Date date, TimeFormatEnum format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getFormat());
        return formatter.format(date);
    }

    public static String convertToString(Date date, DateFormatEnum format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getFormat());
        return formatter.format(date);
    }

    public static String convertToString(Date date, DateTimeFormatEnum format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getFormat());
        return formatter.format(date);
    }
    ///////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    /////////////////////////  构建 Date  ///////////////////////////////
    public static Date ofDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateTimeFormatEnum.YYYY_MM_DDHHmmss.getFormat());
        Date var;
        try {
            var = formatter.parse(date);
        } catch (Exception e) {
            var = null;
        }
        return var;
    }

    public static Date ofDate(String date, DateFormatEnum format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getFormat());
        Date var;
        try {
            var = formatter.parse(date);
        } catch (Exception e) {
            var = null;
        }
        return var;
    }

    public static Date ofDate(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date var;
        try {
            var = formatter.parse(date);
        } catch (Exception e) {
            var = null;
        }
        return var;
    }

    public static Date ofDate(String date, DateTimeFormatEnum format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getFormat());
        Date var;
        try {
            var = formatter.parse(date);
        } catch (Exception e) {
            var = null;
        }
        return var;
    }

    public static Date ofDate(XMLGregorianCalendar cal) {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }
    ////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    /////////////////////////  构建 LocalDate ///////////////////////////////
    public static LocalDate ofLocalDate(String date, DateFormatEnum format) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(format.getFormat());
        return LocalDate.parse(date, var);
    }

    public static LocalDate ofLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }
    ////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    /////////////////////////  构建 LocalDateTime ////////////////////////////
    public static LocalDateTime ofLocalDateTime(String date, DateTimeFormatEnum format) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(format.getFormat());
        return LocalDateTime.parse(date, var);
    }

    public static LocalDateTime ofLocalDateTime(String date, String format) {
        DateTimeFormatter var = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, var);
    }

    public static LocalDateTime ofLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        return LocalDateTime.ofInstant(instant, zone);
    }
    ////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    /////////////////////////  构建 LocalTime ////////////////////////////////
    public static LocalTime ofLocalTime(Date date) {
        LocalDateTime localDateTime = ofLocalDateTime(date);
        return LocalTime.of(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), localDateTime.getNano());
    }
    ////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////
    /////////////////////////   ////////////////////////////////
    public static Integer getPeriodOfDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + Math.abs(day2 - day1);
        } else    //不同年
        {
            return Math.abs(day2 - day1);
        }
    }

    public static Integer getPeriodOfDay(LocalDate date1, LocalDate date2) {
        return 0;
    }

    public static Integer getPeriodOfYear(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return Math.abs(cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR));
    }
    ////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////
    /////////////////////////  日期计算 /////////////////////////////////////
    public static Date plusDay(Date date, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_YEAR, day);

        return cal.getTime();
    }

    public static Date plusHour(Date date, Integer hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.HOUR_OF_DAY, hour);

        return cal.getTime();
    }

    public static Date plusMinute(Date date, Integer minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }
    //////////////////////////////////////////////////////////////////////////

    public static Long getMinuteDiff(Date startTime, Date endTime) {
        Long minutes = null;

        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        long start = c.getTimeInMillis();
        c.setTime(endTime);
        long end = c.getTimeInMillis();
        minutes = (end - start) / (1000L * 60);
        return minutes;
    }

    /**
     * 返回两个时间的相差秒数
     *
     * @param startTime 对比的开始时间
     * @param endTime   对比的结束时间
     * @return 相差秒数
     */
    public static Long getSecondDiff(Date startTime, Date endTime) {

        return (endTime.getTime() - startTime.getTime()) / 1000;
    }

    /**
     * @param startTime: 开始时间
     * @param endTime:   结束时间
     *                   获取两个时间的毫秒数
     **/
    public static Long getSecondMilli(Date startTime, Date endTime) {

        return (endTime.getTime() - startTime.getTime());
    }
}
