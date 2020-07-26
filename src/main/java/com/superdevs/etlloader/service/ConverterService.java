package com.superdevs.etlloader.service;

import com.superdevs.etlloader.dto.CsvToSaveDto;
import com.superdevs.etlloader.model.CSVItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConverterService {
    List<CSVItem> convert(MultipartFile file);
}
