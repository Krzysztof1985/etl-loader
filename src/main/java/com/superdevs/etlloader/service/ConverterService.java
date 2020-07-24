package com.superdevs.etlloader.service;

import com.superdevs.etlloader.dto.CsvToSaveDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConverterService {
    List<CsvToSaveDto> convert(MultipartFile file, String uuid);
}
