package com.superdevs.etlloader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "csv_data")
@CompoundIndexes({
        @CompoundIndex(name = "entry_unique_idx", def = "{ 'dataSource' : 1, 'campaign' : 1, 'daily': 1, 'clicks': 1 , 'impressions' : 1 }", unique = true, background = false)
})
public class CSVItem {
    private String dataSource;
    private String campaign;
    private ZonedDateTime daily;
    private long clicks;
    private long impressions;
}
