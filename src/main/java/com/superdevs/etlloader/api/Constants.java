package com.superdevs.etlloader.api;

public interface Constants {

    interface Tags {
        String ETL_SERVICE = "ETL Service";
    }

    interface Responses {
        String CODE_200 = "OK";
        String CODE_204 = "No content";
        String CODE_400 = "Bad request";
        String CODE_404 = "Not found";
        String CODE_500 = "Internal Server Error";
    }

    interface Descriptions{
        String ETL_SERVICE_DESC = "Service which provides API to upload CSV file and perform some calculations";
    }
}
