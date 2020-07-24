package com.superdevs.etlloader.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CSVItemDto {

    private String uuid;
    private String dataSource;
    private String campaign;
    private String daily;
    private long clicks;
    private long impressions;
}
