package com.thetechmaddy.ecommerce.utils;

import java.time.*;

import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIN;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

public class DateTimeUtils {

    private static final ZoneId ZONE_ID = ZoneOffset.systemDefault();

    public static OffsetDateTime atStartDayOfYear(int year) {
        return atStartDayOfMonth(year, JANUARY.getValue());
    }

    public static OffsetDateTime atEndDayOfYear(int year) {
        return atEndDayOfMonth(year, DECEMBER.getValue());
    }

    public static OffsetDateTime atStartDayOfMonth(int year, int month) {
        LocalDateTime dateTime = toLocalDate(year, month, 1).atTime(MIN);
        return OffsetDateTime.from(ZonedDateTime.of(dateTime, ZONE_ID));
    }

    public static OffsetDateTime atEndDayOfMonth(int year, int month) {
        YearMonth ym = Year.of(year).atMonth(month);
        LocalDateTime dateTime = toLocalDate(ym.getYear(), ym.getMonthValue(), ym.lengthOfMonth()).atTime(MAX);
        return OffsetDateTime.from(ZonedDateTime.of(dateTime, ZONE_ID));
    }

    public static LocalDate toLocalDate(int year, int month, int dayOfMonth) {
        return Year.of(year)
                .atMonth(month)
                .atDay(dayOfMonth);
    }
}
