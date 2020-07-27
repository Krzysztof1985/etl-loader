package com.superdevs.etlloader.util;

import java.time.ZonedDateTime;

/**
 * Interface to convert String to LocalDate with given pattern
 * (pattern depends on implementation)
 * and vice versa
 */
public interface DateUtilFormatter {
    /**
     * converts input to ZonedDateTime
     * @param input string to parse
     * @return parsed local date time
     */
    ZonedDateTime convertDateFromString(String input);

    /**
     * converts zoned date time to string
     * @return string from date with after formatting with pattern
     */
    String convertDateToString(ZonedDateTime input);

}
