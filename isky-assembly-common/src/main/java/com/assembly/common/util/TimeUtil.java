package com.assembly.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 时间工具
 */
public class TimeUtil {



    public static String dataFormatForHour(Date changeDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        String dateString = formatter.format(changeDate);
        return dateString;
    }

    public static Date dateForStart(Date date) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date dateForEnd(Date date) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 时间转换成小时数
     *
     * @param times 时间
     * @return string
     */
    public static String timestamp2HourString(long times) {
        int min = (int) (times / 60);
        int min2 = min % 60;
        int hour = min / 60;
        if (hour == 0) {
            return min2 + "min";
        }
        return hour + "h" + min2 + "min";
    }

    /**
     * 获取后一天
     *
     * @param date 开始日期
     */
    public static Date getLastDay(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        localDate = localDate.plusDays(1);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取当前天数开始的时间
     */
    public static Date getDateTimesWithZero() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDateTime = LocalDate.now();
        ZonedDateTime zonedDateTime = localDateTime.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date getDateTimesWithStart(String date) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = StringUtils.isEmpty(date) ? LocalDateTime.now() : LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd")).atTime(LocalTime.now());
        localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }


    public static Date getDateTimesWithStart(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 时间转成年月日整数,返回的年分未去掉 20
     */
    public static int date2IntWithYearMonthDay(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localdate2Int(localDate, 0);
    }


    /**
     * 获取当前天数结束时间
     */
    public static Date getDateTimesWithEnd() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }


    public static Date getDateTimesWithEnd(int day) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(day).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取当前日期结束时间
     *
     * @param date 日期
     */
    public static Date getDateTimesWithEnd(String date) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = StringUtils.isEmpty(date) ? LocalDateTime.now() : LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd")).atTime(LocalTime.now());
        localDateTime = localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 日期减去相关天数
     *
     * @param start 开始时间
     * @param day 天数
     */
    public static Date dataMinusDays(Date start, int day) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = start.toInstant().atZone(zoneId).toLocalDate().minusDays(day);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date dataMinusHours(Date start, int hour) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = start.toInstant().atZone(zoneId).toLocalDateTime()
                .minusHours(hour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 日期加天数并格式化
     */
    public static String datePlus2String(Date date, int day, String pattern) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = date.toInstant().atZone(zoneId).toLocalDate().plusDays(day);
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期加天数
     */
    public static Date datePlusDay(Date date, int day) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime localDate = date.toInstant().atZone(zoneId).plusDays(day);
        return Date.from(localDate.toInstant());
    }

    /**
     * 日期减天数
     */
    public static Date dateMinusDay(Date date, int day) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime localDate = date.toInstant().atZone(zoneId).minusDays(day);
        return Date.from(localDate.toInstant());
    }

    public static String stringDate2String(String date, String pattern) {
        Date date1 = string2DateByDay(date, "yyyy-MM-dd");
        return datePlus2String(date1, 0, pattern);
    }

    /**
     * 获取当前日期的前几个月
     *
     * @param mouth 日期
     */
    public static Date getCurrentDateMinusMonths(int mouth) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDateTime = LocalDate.now().minusMonths(mouth);
        ZonedDateTime zonedDateTime = localDateTime.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }


    /**
     * 获取当前小时数
     *
     * @return 时间
     */
    public static Date getCurrentByHour() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取当前日期
     */
    public static int getTodayWithInt() {
        LocalDate localDate = LocalDate.now();
        return localdate2Int(localDate);
    }

    /**
     * 把昨天日期转换成int类型
     */
    public static int getYesterdayWithInt() {
        LocalDate localDate = LocalDate.now();
        return localdate2Int(localDate.minusDays(1));
    }

    private static int localdate2Int(LocalDate localDate) {
        return localdate2Int(localDate, 2000);
    }

    private static int localdate2Int(LocalDate localDate, int value) {
        String year = (localDate.getYear() - value) + "";
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        String day = localDate.getDayOfMonth() < 10 ? "0" + localDate.getDayOfMonth() : localDate.getDayOfMonth() + "";
        return Integer.parseInt(year + month + day);
    }

    /**
     * 获取当前小时数
     *
     * @return 时间
     */
    public static Date getCurrentByHour(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = date.toInstant().atZone(zoneId).toLocalDateTime();
        localDateTime = localDateTime.withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取下一个小时
     */
    public static Date getLastHour() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 字符串转日期 （具体到天数）
     *
     * @param date 字符串
     */
    public static Date string2DateByDay(String date) {
        if (StringUtils.isEmpty(date)) {
            return getCurrentByHour();
        }
        return string2DateByDay(date, "yyyy/MM/dd");
    }

    /**
     * 字符串转日期 （具体到天数）
     *
     * @param date 字符串
     */
    public static Date string2DateByDay(String date, String pattern) {
        if (StringUtils.isEmpty(date)) {
            return getCurrentByHour();
        }
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        ZoneId zoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    /**
     * date 转 localDate
     */
    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }


    public static String date2String(int date, String inPattern, String targetPattern) {
        LocalDate localDate = LocalDate.parse(date + "", DateTimeFormatter.ofPattern(inPattern));
        return localDate.format(DateTimeFormatter.ofPattern(targetPattern));
    }

    /**
     * 获取两个时间之间的天数差
     */
    public static int compareDate(Date start, Date end) {
        Instant startInstant = start.toInstant();
        Instant endInstant = end.toInstant();
        return (int) ChronoUnit.DAYS.between(startInstant, endInstant);
    }

    /**
     * 把时间转换成int类型
     */
    public static int date2int(Date date) {
        date = date == null ? new Date() : date;
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = date.toInstant().atZone(zoneId).toLocalDate();
        return localdate2Int(localDate);
    }

    /***
     * 格式化日期：例 2019-03-29 00:00:00
     * @return
     */
    public static Date formatpTime() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);//设置小时数，24小时制
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        return now.getTime();//当天时间
    }

    public static Date formatPreTime() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);//前一天时间
        now.set(Calendar.HOUR_OF_DAY, 0);//设置小时数，24小时制
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        return now.getTime();
    }

    public static Date formatNextTime() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.get(Calendar.DATE) + 1);//下一天时间
        now.set(Calendar.HOUR_OF_DAY, 0);//设置小时数，24小时制
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        return now.getTime();
    }

    /**
     * 判断结束时间是否大于当前开始时间
     */
    public static boolean endTimeLteCurrentDay(Date endDate) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0);
        ZoneId zoneId = ZoneId.systemDefault();
        Instant current = localDateTime.atZone(zoneId).toInstant();
        return current.compareTo(endDate.toInstant()) >= 0;
    }

    /**
     * 判断结束时间是否大于当前开始时间
     */
    public static boolean endTimeLteCurrentDay(LocalDate endDate) {
        LocalDate localDate = LocalDate.now();
        return localDate.isBefore(endDate);
    }


    public static boolean isAfterNow(Date date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.isAfter(now);
    }

    public static boolean isBeforeNowDay(Date date) {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.isBefore(now);
    }

    public static boolean startAfterEnd(Date start, Date end) {
        LocalDateTime startTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return endTime.isAfter(startTime);
    }

    public static boolean isBeforeNow(Date date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.isBefore(now);
    }

    public static int getRemainSecondsOneDay() {
        Date currentDate = new Date();
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault()).plusDays(1L).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int)seconds;
    }


    public static void main(String[] args) {
//        Date date = getCurrentByHour();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(simpleDateFormat.format(date));
//
//        System.out.println(getCurrentByHour());
//        System.out.println(getLastHour());
//
//        System.err.println(getCurrentHourWithInt());
//
//        System.err.println(getCurrentHourWithInt(23));

//        System.out.println(getCurrentDateMinusMonths(3));

//        System.err.println(compareDate(new Date(1543806555000L), new Date(1543992555000L)));

//        System.err.println(dataMinusDays(new Date(), 3));
//
//        System.err.println(dataMinusHours(new Date(), 1));

//        System.err.println(getYesterdayWithInt());
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            String date = scanner.nextLine();
//            try {
//                System.out.println(endTimeLteCurrentDay(simpleDateFormat.parse(date)));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        Date date = new Date(System.currentTimeMillis());
//        Date endDate = new Date(System.currentTimeMillis() + 300000);
//        System.out.println(startAfterEnd(date, endDate));

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String day = scanner.nextLine();
            LocalDate localDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(isBeforeNowDay(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
        }

    }

}
