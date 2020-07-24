package com.superdevs.etlloader.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CsvToSaveDto {

    private String uuid;
    private String dataSource;
    private String campaign;
    private long daily;
    private long clicks;
    private long impressions;
}
