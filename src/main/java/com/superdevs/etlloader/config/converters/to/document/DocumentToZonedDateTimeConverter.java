package com.superdevs.etlloader.config.converters.to.document;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@ReadingConverter
public class DocumentToZonedDateTimeConverter implements Converter<Document, ZonedDateTime> {
    static final String DATE_TIME = "dateTime";
    static final String ZONE = "zone";

    @Override
    public ZonedDateTime convert(@Nullable Document document) {
        if (document == null) return null;
        Date dateTime = document.getDate(DATE_TIME);
        String zoneId = document.getString(ZONE);
        ZoneId zone = ZoneId.of(zoneId);
        return ZonedDateTime.ofInstant(dateTime.toInstant(), zone);
    }
}
