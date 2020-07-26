package com.superdevs.etlloader.filters;

import io.swagger.annotations.ApiModelProperty;

public enum DimensionFilter {

    @ApiModelProperty("Campaign")
    CAMPAIGN("campaign"),

    @ApiModelProperty("DataSource")
    DATASOURCE("dataSource");

    private String name;

    DimensionFilter(String name) {
        this.name = name;
    }
}
