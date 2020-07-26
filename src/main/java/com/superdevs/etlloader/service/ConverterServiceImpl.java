package com.superdevs.etlloader.service;

import com.superdevs.etlloader.model.CSVItem;
import com.superdevs.etlloader.util.CSVFileHeaders;
import com.superdevs.etlloader.util.DateUtilFormatter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ConverterServiceImpl implements ConverterService {

    private DateUtilFormatter formatter;

    @Override
    public List<CSVItem> convert(MultipartFile file) {

        List<CSVItem> output = new ArrayList<>();
        try (Reader inputStreamReader = new InputStreamReader(file.getInputStream())) {
            CSVFormat.DEFAULT
                    .withHeader(CSVFileHeaders.class)
                    .withFirstRecordAsHeader()
                    .parse(inputStreamReader)
                    .forEach(item -> {
                        String ds = item.get(CSVFileHeaders.Datasource);
                        String camp = item.get(CSVFileHeaders.Campaign);
                        ZonedDateTime daily = convertToDecimalDate(item.get(CSVFileHeaders.Daily));
                        long clicks = Long.valueOf(item.get(CSVFileHeaders.Clicks));
                        long impressions = Long.valueOf(item.get(CSVFileHeaders.Impressions));

                        CSVItem entity = CSVItem.builder()
                                .campaign(camp)
                                .clicks(clicks)
                                .daily(daily)
                                .impressions(impressions)
                                .dataSource(ds)
                                .build();
                        output.add(entity);
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }


    private ZonedDateTime convertToDecimalDate(String input) {
        return formatter.convertDateFromString(input);
    }
}
