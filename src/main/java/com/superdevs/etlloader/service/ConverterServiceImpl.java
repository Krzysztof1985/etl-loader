package com.superdevs.etlloader.service;

import com.superdevs.etlloader.dto.CsvToSaveDto;
import com.superdevs.etlloader.util.CSVFileHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ConverterServiceImpl implements ConverterService {

    //    private static final String DATE_FORMAT = "dd/MM/yy";
    private final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("dd/MM/yy'T'HH:mm:ss");

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    @Override
    public List<CsvToSaveDto> convert(MultipartFile file, String uuid) {

        List<CsvToSaveDto> output = new ArrayList<>();
        try (Reader inputStreamReader = new InputStreamReader(file.getInputStream())) {
            CSVFormat.DEFAULT
                    .withHeader(CSVFileHeaders.class)
                    .withFirstRecordAsHeader()
                    .parse(inputStreamReader)
                    .forEach(item -> {
                        String ds = item.get(CSVFileHeaders.Datasource);
                        String camp = item.get(CSVFileHeaders.Campaign);
                        long daily = convertToDecimalDate(item.get(CSVFileHeaders.Daily));
                        long clicks = Long.valueOf(item.get(CSVFileHeaders.Clicks));
                        long impressions = Long.valueOf(item.get(CSVFileHeaders.Impressions));
                        CsvToSaveDto csvItemDto = new CsvToSaveDto(uuid, ds, camp, daily, clicks, impressions);
                        output.add(csvItemDto);
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }


    private long convertToDecimalDate(String input) {
        try {
            Date parse = sdf.parse(input);
            return parse.getTime();
        } catch (ParseException e) {
            log.error("Unable to convert {} to big decimal ", input);
            throw new RuntimeException("Parsing error");
        }
    }
}
