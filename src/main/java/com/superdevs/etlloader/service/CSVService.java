package com.superdevs.etlloader.service;

import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.model.CSVItem;

import java.util.List;

public interface CSVService {

    void saveAllCSVItems(List<CSVItem> items);

    List<CSVItemDto> getAllItemsForWorkId(String uuid);

    List<CSVItemDto> countTotalClicks(String datasource, String from, String to);
}
