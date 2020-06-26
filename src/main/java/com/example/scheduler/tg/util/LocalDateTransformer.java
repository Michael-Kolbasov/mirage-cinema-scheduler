package com.example.scheduler.tg.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDateTransformer {
    private static final String SPACE = " ";
    private static final String COMMA = ",";

    private static final Map<LocalDate, String> CACHE = new ConcurrentHashMap<>();

    public static String transform(LocalDate date) {
        if (CACHE.containsKey(date)) {
            return CACHE.get(date);
        }

        StringBuilder builder = new StringBuilder();
        int dayOfMonth = date.getDayOfMonth();
        Month month = date.getMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        String monthConverted = convertMonth(month);
        String dayOfWeekConverted = convertDayOfWeek(dayOfWeek);

        builder.append(dayOfMonth)
                .append(SPACE)
                .append(monthConverted)
                .append(COMMA)
                .append(SPACE)
                .append(dayOfWeekConverted);
        String dateConverted = builder.toString();

        CACHE.put(date, dateConverted);

        return dateConverted;
    }

    private static String convertMonth(Month month) {
        switch (month) {
            case JANUARY:   return "января";
            case FEBRUARY:  return "февраля";
            case MARCH:     return "марта";
            case APRIL:     return "апреля";
            case MAY:       return "мая";
            case JUNE:      return "июня";
            case JULY:      return "июля";
            case AUGUST:    return "августа";
            case SEPTEMBER: return "сентября";
            case OCTOBER:   return "октября";
            case NOVEMBER:  return "ноября";
            case DECEMBER:  return "декабря";
            default:        throw new RuntimeException("Unexpected month: " + month);
        }
    }

    private static String convertDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:    return "пн";
            case TUESDAY:   return "вт";
            case WEDNESDAY: return "ср";
            case THURSDAY:  return "чт";
            case FRIDAY:    return "пт";
            case SATURDAY:  return "сб";
            case SUNDAY:    return "вс";
            default:        throw new RuntimeException("Unexpected day of week: " + dayOfWeek);
        }
    }
}
