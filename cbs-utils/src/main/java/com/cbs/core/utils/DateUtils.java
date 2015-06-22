package com.cbs.core.utils;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Pavel Spiridonov on 22.06.2015.
 */
public class DateUtils {

    public static final String DEFAULT_NULL_VALUE_PLACEHOLDER = null;
    public final static int QUARTER_COUNT_IN_YEAR = 4;
    public static final String LOCALE_LANG = "en";

    public static int getCurrentYear() {
        return new DateTime().getYear();
    }

    public static Interval yearInterval(@NotNull Instant date){
        return new Interval(firstInstantOfYear(date), lastInstantOfYear(date));
    }

    public static Interval yearInterval(int year){
        return new Interval(firstInstantOfYear(year), lastInstantOfYear(year));
    }

    public static Interval monthInterval(@NotNull Instant date){
        return new Interval(firstInstantOfMonth(date), lastInstantOfMonth(date));
    }

    public static Interval monthInterval(int year, int month){
        return new Interval(firstInstantOfMonth(year, month), lastInstantOfMonth(year, month));
    }

    public static Interval quarterInterval(@NotNull Instant date){
        return new Interval(firstInstantOfQuarter(date), lastInstantOfQuarter(date));
    }

    public static Instant firstInstantOfYear(int year) {
        return firstDateTimeOfYear(year).toInstant();
    }

    public static Instant lastInstantOfYear(int year) {
        return firstDateTimeOfYear(year).plusYears(1).minusMillis(1).toInstant();
    }

    public static Instant firstInstantOfMonth(int year, int month) {
        return firstDateTimeOfMonth(year, month).toInstant();
    }

    public static Instant firstInstantOfYear(@NotNull Instant instant) {
        return firstDateTimeOfYear(instant).toInstant();
    }

    public static Instant lastInstantOfYear(@NotNull Instant instant) {
        return firstDateTimeOfYear(instant).plusYears(1).minusMillis(1).toInstant();
    }

    public static Instant firstInstantOfMonth(@NotNull Instant instant) {
        return firstDateTimeOfMonth(instant).toInstant();
    }

    public static Instant lastInstantOfMonth(int year, int month) {
        return firstDateTimeOfMonth(year, month).plusMonths(1).minusMillis(1).toInstant();
    }

    public static Instant lastInstantOfMonth(@NotNull Instant instant) {
        return firstDateTimeOfMonth(instant).plusMonths(1).minusMillis(1).toInstant();
    }

    public static DateTime firstDateTimeOfYear(@NotNull Instant instant) {
        return instant.toDateTime().withDayOfYear(1).withTimeAtStartOfDay();
    }

    public static DateTime firstDateTimeOfMonth(@NotNull Instant instant) {
        return instant.toDateTime().withDayOfMonth(1).withTimeAtStartOfDay();
    }

    public static boolean isYearEqualInstantYear(Instant instant, int year) {
        return (year == instant.toDateTime().getYear());
    }

    public static boolean isYearBeforeInstantYear(Instant instant, int year) {
        return (year < instant.toDateTime().getYear());
    }

    public static int getCurrentQuarter() {
        return dateTimeToQuarterNumber(DateTime.now());
    }

    public static int getQuarter(@NotNull DateTime dateTime) {
        return dateTimeToQuarterNumber(dateTime);
    }

    public static int getQuarter(@NotNull Instant instant) {
        return dateTimeToQuarterNumber(instant.toDateTime());
    }

    public static boolean isCurrentYear(int year) {
        return year == DateTime.now().getYear();
    }

    public static boolean isCurrentQuarter(int year, int quarterNum) {
        return year == DateTime.now().getYear() && quarterNum == getCurrentQuarter();
    }

    public static int getQuarterStartMonth(int quarterNumber) {
        validateQuarterNumber(quarterNumber);
        return (quarterNumber - 1) * 3 + 1;
    }

    public static int getQuarterEndMonth(int quarterNumber) {
        validateQuarterNumber(quarterNumber);
        return quarterNumber * 3;
    }

    public static String instantToDateString(Instant instant) {
        return instantToDateString(instant, DEFAULT_NULL_VALUE_PLACEHOLDER);
    }

    public static String instantToDateString(Instant instant, String nullValuePlaceholder) {
        return instantToDateString(instant, Patterns.DD_MM_YYYY, nullValuePlaceholder);
    }

    public static String instantToDateString(Instant instant, String pattern, String nullValuePlaceholder) {
        return instant == null ? nullValuePlaceholder : dateTimeToDateString(instant.toDateTime(), pattern, nullValuePlaceholder);
    }

    public static String dateTimeToDateString(DateTime dateTime) {
        return dateTimeToDateString(dateTime, DEFAULT_NULL_VALUE_PLACEHOLDER);
    }

    public static String dateTimeToDateString(DateTime dateTime, String nullValuePlaceholder) {
        return dateTimeToDateString(dateTime, Patterns.DD_MM_YYYY, nullValuePlaceholder);
    }

    public static String dateTimeToDateString(DateTime dateTime, String pattern, String nullValuePlaceholder) {
        return dateTime == null ? nullValuePlaceholder : dateTime.toDateTime().toString(getFormatter(pattern));
    }

    public static String dateTimeToFullTimeString(DateTime dateTime) {
        return dateTimeToFullTimeString(dateTime, DEFAULT_NULL_VALUE_PLACEHOLDER);
    }

    public static String dateTimeToFullTimeString(DateTime dateTime, String nullValuePlaceholder) {
        return dateTimeToDateString(dateTime, Patterns.DD_MM_YY_HH_MM_SS, nullValuePlaceholder);
    }

    public static Instant dateStringToInstant(String dateStr) {
        return dateStringToInstant(dateStr, Patterns.DD_MM_YY);
    }

    public static Instant dateStringToInstant(String dateStr, String pattern) {
        return (ObjectUtils.equals(DEFAULT_NULL_VALUE_PLACEHOLDER, dateStr) || StringUtils.isBlank(dateStr)) ? null : Instant.parse(dateStr, getFormatter(pattern));
    }

    public static DateTime dateStringToDateTime(String dateStr) {
        return dateStringToDateTime(dateStr, Patterns.DD_MM_YY);
    }

    public static DateTime dateStringToDateTime(String dateStr, String pattern) {
        return (ObjectUtils.equals(DEFAULT_NULL_VALUE_PLACEHOLDER, dateStr) || StringUtils.isBlank(dateStr)) ? null : DateTime.parse(dateStr, getFormatter(pattern));
    }

    public static Instant dateStringNoYearToInstant(String dateStr, String pattern, int year) {
        DateTime dateTime = dateStringToDateTime(dateStr, pattern);
        return dateTime == null ? null : dateTime.withYear(year).toInstant();
    }

    /**
     * @param quarterNumber 1-based number
     * @param year          year number
     * @return the first instant of quarter specified by method arguments
     */
    public static Instant firstInstantOfQuarter(int quarterNumber, int year) {
        validateQuarterNumber(quarterNumber);
        return firstDateTimeOfQuarter(quarterNumber, year).toInstant();
    }

    /**
     * @param quarterNumber 1-based number
     * @param year          year number
     * @return the last instant of quarter specified by method arguments
     */
    public static Instant lastInstantOfQuarter(int quarterNumber, int year) {
        validateQuarterNumber(quarterNumber);
        return firstDateTimeOfQuarter(quarterNumber, year).plusMonths(3).minusMillis(1).toInstant();
    }

    public static Instant firstInstantOfQuarter(Instant instant) {
        Validate.notNull(instant);
        int quarterNumber = instantToQuarterNumber(instant);
        int year = instant.toDateTime().get(DateTimeFieldType.year());
        return firstDateTimeOfQuarter(quarterNumber, year).toInstant();
    }

    public static Instant lastInstantOfQuarter(Instant instant) {
        Validate.notNull(instant);
        int quarterNumber = instantToQuarterNumber(instant);
        int year = instant.toDateTime().get(DateTimeFieldType.year());
        return firstDateTimeOfQuarter(quarterNumber, year).plusMonths(3).minusMillis(1).toInstant();
    }

    private static int getMonthNumber(int quarterNumber) {
        return (quarterNumber - 1) * 3 + 1;
    }

    /**
     * @param instant instant at timeline
     * @return 1-based number quarter number
     */
    public static int instantToQuarterNumber(Instant instant) {
        Validate.notNull(instant);
        return dateTimeToQuarterNumber(instant.toDateTime());
    }

    /**
     * @param dateTime date and time
     * @return 1-based number quarter number
     */
    private static int dateTimeToQuarterNumber(@NotNull DateTime dateTime) {
        return ((dateTime.getMonthOfYear() - 1) / 3) + 1;
    }

    private static DateTime firstDateTimeOfYear(int year) {
        return firstDateTimeOfMonth(year, 1);
    }

    private static DateTime firstDateTimeOfQuarter(int quarterNumber, int year) {
        return firstDateTimeOfMonth(year, getMonthNumber(quarterNumber));
    }

    private static DateTime firstDateTimeOfMonth(int year, int month) {
        return new DateTime(year, month, 1, 0, 0);
    }

    private static DateTimeFormatter getFormatter(String pattern) {
        return DateTimeFormat.forPattern(pattern).withLocale(Locale.forLanguageTag(LOCALE_LANG));
    }

    private static void validateQuarterNumber(int quarterNumber) {
        Validate.isTrue(quarterNumber > 0 && quarterNumber <= QUARTER_COUNT_IN_YEAR, "Quarter number should be between 1 and " + QUARTER_COUNT_IN_YEAR);
    }

    public static class Patterns {
        public static final String YY = "yy";
        public static final String DD_MM = "dd/MM";
        public static final String DD_MM_YY = "dd/MM/yy";
        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String DDMMYYYY = "ddMMyyyy";
        public static final String MMYYYY = "MMyyyy";
        public static final String YYYYMM = "yyyyMM";
        public static final String MM_YYYY = "MM/yyyy";
        public static final String MM_YY = "MM/yy";
        public static final String DD_MM_YY_HH_MM_SS = "dd/MM/yy HH:mm:ss";
        public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
        public static final String DD_MMMM_YYYY = "dd MMMM yyyy";
        public static final String DEFAULT_JS_FORMAT = "dd/MM/yyyy";
    }

}
