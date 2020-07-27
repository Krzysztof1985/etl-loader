package com.superdevs.etlloader.service;

import com.superdevs.etlloader.filters.MainFilter;
import com.superdevs.etlloader.model.CSVItem;
import org.bson.Document;

import java.util.List;

public interface CSVService {

    void saveAllCSVItems(List<CSVItem> items);

    void deleteAllItems();

    List<Document> generateAggregatedData(MainFilter mainFilter);
}
