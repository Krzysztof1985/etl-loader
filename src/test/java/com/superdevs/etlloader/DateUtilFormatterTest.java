package com.superdevs.etlloader;

import com.superdevs.etlloader.util.DataUtilFormatterImpl;
import com.superdevs.etlloader.util.DateUtilFormatter;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test without spring context to setup and speed up the tests
 */
public class DateUtilFormatterTest {

    private DateUtilFormatter dateUtilFormatter = new DataUtilFormatterImpl();

    @Test
    public void convertDateFromString_shouldProperlyFormatStringToDateTest() {
        ZonedDateTime zonedDateTime = dateUtilFormatter.convertDateFromString("01/11/20");
        assertThat(zonedDateTime).satisfies(x -> {
            int dayOfMonth = x.getDayOfMonth();
            int month = zonedDateTime.getMonthValue();
            int year = zonedDateTime.getYear();
            assertThat(dayOfMonth).isEqualTo(11);
            assertThat(month).isEqualTo(1);
            assertThat(year).isEqualTo(2020);
        });
    }

    @Test
    public void convertDateToString_shouldProperlyConvertZonedDateToString(){
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(1985, 06, 1, 0, 0, 0, 0, utcZone);
        String output = dateUtilFormatter.convertDateToString(zonedDateTime);
        assertThat(output).isEqualTo("06/01/85");
    }
}
