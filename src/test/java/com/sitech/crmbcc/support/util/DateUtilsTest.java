package com.sitech.crmbcc.support.util;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 13:53
 */
class DateUtilsTest {

    @Test
    void year() {
        System.out.println(DateUtils.year(DateUtils.current()));
    }

    @Test
    void quarter() {
        System.out.println(DateUtils.quarter(DateUtils.current()));
    }

    @Test
    void month() {
        System.out.println(DateUtils.month(DateUtils.current()));
    }

    @Test
    void dayOfYear() {
        System.out.println(DateUtils.dayOfYear(DateUtils.current()));
    }

    @Test
    void dayOfMonth() {
        System.out.println(DateUtils.dayOfMonth(DateUtils.current()));
    }

    @Test
    void dayOfWeek() {
        System.out.println(DateUtils.dayOfWeek(DateUtils.current()));
    }

    @Test
    void nextMonth() {
        System.out.println(DateUtil.beginOfYear(new Date()));
        System.out.println(DateUtil.endOfYear(new Date()));
    }

    @Test
    void now() {
        // given

        // when

        // then
    }

    @Test
    void hour() {
        System.out.println(DateUtil.hour(new Date(2022, Calendar.JUNE, 25, 12, 15), false));
        System.out.println(DateUtils.hour(DateUtils.current()));
    }

    @Test
    void testHour() {
        System.out.println(DateUtils.hour(LocalDateTime.of(2022, 8, 10, 0, 30, 50), false));
    }

    @Test
    void toDate() {
    }

    @Test
    void testToDate() {
//        DateUtil.beginOfYear()
    }

    @Test
    void toLocalDate() {
        // given

        // when

        // then
    }

    @Test
    void testToLocalDate() {
        // given

        // when

        // then
    }

    @Test
    void toLocalDateTime() {
        // given

        // when

        // then
    }

    @Test
    void testToLocalDateTime() {
        // given

        // when

        // then
    }

    @Test
    void minute() {
        System.out.println(DateUtils.minute(LocalDateTime.of(2022, 8, 10, 0, 30, 50)));
        System.out.println(DateUtils.minute(LocalDateTime.now()));
    }

    @Test
    void second() {
        System.out.println(DateUtils.second(LocalDateTime.of(2022, 8, 10, 0, 30, 50)));
        System.out.println(DateUtils.second(LocalDateTime.now()));
    }

    @Test
    void millisecond() {
        System.out.println(DateUtils.millisecond(LocalDateTime.now()));
        System.out.println(DateUtil.millisecond(new Date()));
    }

    @Test
    void isAM() {
        System.out.println(DateUtils.isAM(LocalDateTime.now()));
    }

    @Test
    void isPM() {
        System.out.println(DateUtils.isPM(LocalDateTime.now()));
    }

    @Test
    void current() {
        // given

        // when

        // then
    }

    @Test
    void currentYear() {
        // given

        // when

        // then
    }

    @Test
    void currentQuarter() {
        // given

        // when

        // then
    }

    @Test
    void currentMonth() {
        // given

        // when

        // then
    }

    @Test
    void currentDayOfYear() {
        // given

        // when

        // then
    }

    @Test
    void currentDayOfMonth() {
        // given

        // when

        // then
    }

    @Test
    void currentDayOfWeek() {
        // given

        // when

        // then
    }

    @Test
    void currentHour() {
        // given

        // when

        // then
    }

    @Test
    void currentMinute() {
        // given

        // when

        // then
    }

    @Test
    void currentSecond() {
        // given

        // when

        // then
    }

    @Test
    void currentMillisecond() {
        // given

        // when

        // then
    }

    @Test
    void beginOfYear() {
        System.out.println(DateUtil.beginOfYear(new Date()));
        System.out.println(DateUtils.firstDayOfYear(LocalDateTime.now()));
    }

    @Test
    void endOfYear() {
        System.out.println(DateUtils.lastDayOfYear(LocalDateTime.now()));
        System.out.println(DateUtil.endOfYear(new Date()));
    }

    @Test
    void firstDayOfYear() {
    }

    @Test
    void lastDayOfYear() {
    }

    @Test
    void firstDayOfNextYear() {
        System.out.println(DateUtils.firstDayOfNextYear(LocalDateTime.now()));
    }

    @Test
    void lastDayOfNextYear() {
        System.out.println(DateUtils.lastDayOfNextYear(LocalDateTime.now()));
    }

    @Test
    void firstDayOfPrevYear() {
        System.out.println(DateUtils.firstDayOfPrevYear(LocalDateTime.now()));
    }

    @Test
    void lastDayOfPrevYear() {
        System.out.println(DateUtils.lastDayOfPrevYear(LocalDateTime.now()));
    }

    @Test
    void isLeapYear() {
        System.out.println(DateUtil.isLeapYear(2008));
        System.out.println(DateUtils.isLeapYear(2008));
    }

    @Test
    void lengthOfYear() {
        System.out.println(DateUtils.lengthOfYear(2022));
        System.out.println(DateUtil.lengthOfYear(2022));
    }

    @Test
    void betweenYear() {
        System.out.println(DateUtil.betweenYear(new Date(2021, 8, 10), new Date(), true));
    }

    @Test
    void firstDayOfMonth() {
        System.out.println(DateUtils.firstDayOfMonth(LocalDateTime.now()));
        System.out.println(DateUtil.beginOfMonth(new Date()));
    }

    @Test
    void lastDayOfMonth() {
        System.out.println(DateUtils.lastDayOfMonth(LocalDateTime.now()));
        System.out.println(DateUtil.endOfMonth(new Date()));
    }

    @Test
    void firstDayOfNextMonth() {
        System.out.println(DateUtils.firstDayOfNextMonth(LocalDateTime.now()));
    }

    @Test
    void lastDayOfNextMonth() {
        System.out.println(DateUtils.lastDayOfNextMonth(LocalDateTime.of(2022,1,6,10,20)));
    }

    @Test
    void firstDayOfPrevMonth() {
        System.out.println(DateUtils.firstDayOfPrevMonth(LocalDateTime.now()));
    }

    @Test
    void lastDayOfPrevMonth() {
        System.out.println(DateUtils.lastDayOfPrevMonth(LocalDateTime.now()));
    }

    @Test
    void lengthOfMonth() {
        System.out.println(DateUtils.lengthOfMonth(2, false));
    }
}