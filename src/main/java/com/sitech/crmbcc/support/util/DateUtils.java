package com.sitech.crmbcc.support.util;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

/**
 * 日期工具类
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 11:02
 */
public class DateUtils {

    // 获取指定日期相关信息

    /**
     * 获取指定日期所在的年份
     *
     * @param localDateTime 日期
     * @return 年份
     */
    public static int year(LocalDateTime localDateTime) {
        return localDateTime.getYear();
    }

    /**
     * 获取指定日期所在的季度，范围为 1-4
     *
     * @param localDateTime 日期
     * @return 季度
     */
    public static int quarter(LocalDateTime localDateTime) {
        return (localDateTime.getMonthValue() - 1) / 3 + 1;
    }

    /**
     * 获取指定日期所在的月份，范围为 1-12
     *
     * @param localDateTime 日期
     * @return 月份
     */
    public static int month(LocalDateTime localDateTime) {
        return localDateTime.getMonthValue();
    }

    /**
     * 获取指定日期是这个日期所在年份的第几天，范围为 1-366
     *
     * @param localDateTime 日期
     * @return 天
     */
    public static int dayOfYear(LocalDateTime localDateTime) {
        return localDateTime.getDayOfYear();
    }

    /**
     * 获取指定日期是这个日期所在月份的第几天，范围为 1-31
     *
     * @param localDateTime 日期
     * @return 天
     */
    public static int dayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.getDayOfMonth();
    }

    /**
     * 获取指定日期是这个日期所在星期的第几天，范围为 1-7
     *
     * @param localDateTime 日期
     * @return 天
     */
    public static int dayOfWeek(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getValue();
    }

    /**
     * 获取指定日期的小时数，范围为 0-23 (24 小时制)
     *
     * @param localDateTime 日期
     * @return 小时
     */
    public static int hour(LocalDateTime localDateTime) {
        return hour(localDateTime, true);
    }

    /**
     * 获取指定日期的小时数
     * <p>1. 是 24 小时制范围为：0-23</p>
     * <p>2. 不是 24 小时制范围为：0-11</p>
     *
     * @param localDateTime 日期
     * @param is24HourClock 是否为 24 小时制
     * @return 小时
     */
    public static int hour(LocalDateTime localDateTime, boolean is24HourClock) {
        return is24HourClock ? localDateTime.getHour() : localDateTime.get(ChronoField.HOUR_OF_AMPM);
    }

    /**
     * 获取指定日期的分钟数，范围为 0-59
     *
     * @param localDateTime 日期
     * @return 分钟
     */
    public static int minute(LocalDateTime localDateTime) {
        return localDateTime.getMinute();
    }

    /**
     * 获取指定日期的秒数，范围为 0-59
     *
     * @param localDateTime 日期
     * @return 秒
     */
    public static int second(LocalDateTime localDateTime) {
        return localDateTime.getSecond();
    }

    /**
     * 获取指定日期的毫秒数
     *
     * @param localDateTime 日期
     * @return 毫秒数
     */
    public static int millisecond(LocalDateTime localDateTime) {
        return localDateTime.get(ChronoField.MILLI_OF_SECOND);
    }

    /**
     * 判断指定日期是否为上午
     *
     * @param localDateTime 日期
     * @return 是否为上午
     */
    public static boolean isAM(LocalDateTime localDateTime) {
        return isAMPM(localDateTime) == 0;
    }

    /**
     * 判断指定日期是否为下午
     *
     * @param localDateTime 日期
     * @return 是否为上午
     */
    public static boolean isPM(LocalDateTime localDateTime) {
        return isAMPM(localDateTime) == 1;
    }

    private static int isAMPM(LocalDateTime localDateTime) {
        return localDateTime.get(ChronoField.AMPM_OF_DAY);
    }

    // 获取当前日期相关信息

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDateTime current() {
        return LocalDateTime.now();
    }

    public static int currentYear() {
        return year(LocalDateTime.now());
    }

    public static int currentQuarter() {
        return quarter(LocalDateTime.now());
    }

    public static int currentMonth() {
        return year(LocalDateTime.now());
    }

    public static int currentDayOfYear() {
        return dayOfYear(LocalDateTime.now());
    }

    public static int currentDayOfMonth() {
        return dayOfMonth(LocalDateTime.now());
    }

    public static int currentDayOfWeek() {
        return dayOfWeek(LocalDateTime.now());
    }

    public static int currentHour() {
        return hour(LocalDateTime.now());
    }

    public static int currentMinute() {
        return minute(LocalDateTime.now());
    }

    public static int currentSecond() {
        return second(LocalDateTime.now());
    }

    public static int currentMillisecond() {
        return millisecond(LocalDateTime.now());
    }

    // 年份相关操作功能增强

    /**
     * 获取某年的第一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime firstDayOfYear(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取某年的最后一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime lastDayOfYear(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取下一年的第一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime firstDayOfNextYear(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfNextYear());
    }

    /**
     * 获取下一年的最后一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime lastDayOfNextYear(LocalDateTime localDateTime) {
        return localDateTime.with(temporal -> temporal.plus(1, YEARS).with(TemporalAdjusters.lastDayOfYear()));
    }

    /**
     * 获取上一年的第一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime firstDayOfPrevYear(LocalDateTime localDateTime) {
        return localDateTime.with(temporal -> temporal.minus(1, YEARS).with(TemporalAdjusters.firstDayOfYear()));
    }

    /**
     * 获取上一年的最后一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime lastDayOfPrevYear(LocalDateTime localDateTime) {
        return localDateTime.with(temporal -> temporal.minus(1, YEARS).with(TemporalAdjusters.lastDayOfYear()));
    }

    /**
     * 判断是否为闰年
     *
     * @param year 年份
     * @return 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * 获取年份的总天数
     *
     * @param year 年
     * @return 总天数
     */
    public static int lengthOfYear(int year) {
        return Year.of(year).length();
    }

    // 月份相关操作功能增强

    /**
     * 获取某月的第一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime firstDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取某月的最后一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime lastDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取下个月的第一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime firstDayOfNextMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * 获取下个月的最后一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime lastDayOfNextMonth(LocalDateTime localDateTime) {
        return localDateTime.with(temporal -> temporal.plus(1, MONTHS).with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * 获取上个月的第一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime firstDayOfPrevMonth(LocalDateTime localDateTime) {
        return localDateTime.with(temporal -> temporal.minus(1, MONTHS).with(TemporalAdjusters.firstDayOfMonth()));
    }

    /**
     * 获取上个月的最后一天
     *
     * @param localDateTime 日期
     * @return 日期
     */
    public static LocalDateTime lastDayOfPrevMonth(LocalDateTime localDateTime) {
        return localDateTime.with(temporal -> temporal.minus(1, MONTHS).with(TemporalAdjusters.lastDayOfMonth()));
    }

    /**
     * 获取月份的总天数
     *
     * @param month    月份
     * @param leapYear 是否闰年
     * @return 总天数
     */
    public static int lengthOfMonth(int month, boolean leapYear) {
        return Month.of(month).length(leapYear);
    }

    // Date、LocalDate、LocalDateTime 之间的转换

    public static Date toDate(LocalDate localDate) {
        return Date.from(Instant.from(localDate));
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(Instant.from(localDateTime));
    }

    public static LocalDate toLocalDate(Date date) {
        return LocalDate.from(date.toInstant());
    }

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return LocalDate.from(localDateTime);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.from(date.toInstant());
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return LocalDateTime.from(localDate);
    }
}
