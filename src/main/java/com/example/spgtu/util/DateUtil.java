package com.example.spgtu.util;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static SimpleDateFormat formatDateAndTimeSS = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static SimpleDateFormat formatDateAndTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat formatYear1 = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat formatDateDay = new SimpleDateFormat("dd");
    private static SimpleDateFormat formatMonth = new SimpleDateFormat("M");
    private static SimpleDateFormat formatYear = new SimpleDateFormat("Y");
    private static SimpleDateFormat formatYear321 = new SimpleDateFormat("Y");

    public static String getDbMmYyyyHhMmSs(Date date) {
        if (date == null)
            return null;
        formatDateAndTimeSS.setLenient(false);
        return formatDateAndTimeSS.format(date);
    }

    public static String getYear(Date date){
        if (date == null)
            return null;
        formatYear.setLenient(false);
        return formatYear.format(date);
    }
    public static String getYear1(Date date){
        if (date == null)
            return null;
        formatYear321.setLenient(false);
        return formatYear321.format(date);
    }

    public static String getDayDate(Date date) {
        if (date == null)
            return null;
        formatDate.setLenient(false);
        return formatDateDay.format(date);
    }


    public static String getDayDate1(Date date) {
        if (date == null)
            return null;
        formatDate.setLenient(false);
        return formatDate.format(date);
    }

    public static String getMonth(Date date) {
        if (date == null)
            return null;
        formatMonth.setLenient(false);
        return formatMonth.format(date);
    }
    public static String getMonthStrAndYear(Date date) {
        if (date == null)
            return null;
        Month jan = Month.of(Integer.parseInt(getMonth(date)));
        Locale loc = Locale.forLanguageTag("ru");
        return jan.getDisplayName(TextStyle.FULL_STANDALONE, loc) + " " + getYear1(date);
    }

    public static String getString(Date date, String format) {
        return getResult(new SimpleDateFormat(format), date);
    }

    private static String getResult(SimpleDateFormat simpleDateFormat, Date date) {
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.format(date);
    }

    public static String getTimeDate(Date date) {
        return getString(date, "HH:mm:ss");
    }

}
