package com.superdevs.etlloader.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;

@Component
public class DataUtilFormatterImpl implements DateUtilFormatter {

    private final DateTimeFormatter formatter;
    private static final String DATE_PATTERN = "MM/dd/yy";

    public DataUtilFormatterImpl() {
        this.formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    }

    @Override
    public ZonedDateTime convertDateFromString(String input) {
        ZonedDateTime zonedDateTime = LocalDate.parse(input, formatter)
                .atStartOfDay(ZoneOffset.UTC);
        return zonedDateTime;
    }

    @Override
    public String convertDateToString(ZonedDateTime input) {
        String day = String.valueOf(input.getDayOfMonth());
        String month = String.valueOf(input.getMonthValue());

        String validMonth = addMissingZeroBiFun.apply(month, 1);
        String validDay = addMissingZeroBiFun.apply(day, 1);

        String year = String.valueOf(input.getYear()).substring(2, 4);
        String result = validMonth + "/" + validDay + "/" + year;
        return result;
    }

    private BiFunction<String, Integer, String> addMissingZeroBiFun = (s, len) -> s.length() == len ? "0" + s : s;
}
