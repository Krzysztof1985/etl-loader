package com.superdevs.etlloader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "csv_data")
public final class CSVItem {

    @Id
    private String id;
    @Indexed(name = "uid", unique = true, background = true)
    private String uuid;
    @Indexed(name="dataSource_idx", background = true)
    private String dataSource;
    @Indexed(name="campaign_idx", background = true)
    private String campaign;
    private long daily;
    private long clicks;
    private long impressions;
}
