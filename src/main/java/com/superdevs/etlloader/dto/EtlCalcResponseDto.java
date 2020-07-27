package com.superdevs.etlloader.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtlCalcResponseDto {
    String name;
    Long clicks;
    Long impressions;
}
