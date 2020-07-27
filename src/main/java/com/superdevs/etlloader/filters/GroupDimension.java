package com.superdevs.etlloader.filters;

public enum GroupDimension {

    DATASOURCE("dataSource"),
    CAMPAIGN("campaign");


    private String name;

    GroupDimension(String name) {
        this.name = name;
    }
}
