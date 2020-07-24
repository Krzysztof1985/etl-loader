package com.superdevs.etlloader.service;

import com.mongodb.client.model.Filters;
import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.dto.CsvToSaveDto;
import com.superdevs.etlloader.model.CSVItem;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CSVServiceImpl implements CSVService {

    private MongoTemplate mongoTemplate;

    private MapperFacade mapper;

    @Override
    public void saveAllCSVItems(List<CsvToSaveDto> items) {
        List<CSVItem> csvItems = mapper.mapAsList(items, CSVItem.class);
        csvItems.forEach(item -> mongoTemplate.save(item));
    }

    @Override
    public List<CSVItemDto> getAllItemsForWorkId(String uuid) {
        Query query = Query.query(Criteria.where("uuid").is(uuid));
        List<CSVItem> csvItems = mongoTemplate.find(query, CSVItem.class);

        List<CSVItemDto> csvItemDtos = mapper.mapAsList(csvItems, CSVItemDto.class);
        return csvItemDtos;
    }

    @Override
    public List<CSVItemDto> countTotalClicks(String uuid, String datasource, Long from, Long to) {
        Criteria criteria = Criteria.where("uuid").is(uuid)
                .and("dataSource").is(datasource)
                .andOperator(Criteria.where("daily").gt(from).lte(to));

        Query query = Query.query(criteria);
        List<CSVItem> csvItems = mongoTemplate.find(query, CSVItem.class);
        List<CSVItemDto> csvItemDtos = mapper.mapAsList(csvItems, CSVItemDto.class);
        return csvItemDtos;
    }
}
