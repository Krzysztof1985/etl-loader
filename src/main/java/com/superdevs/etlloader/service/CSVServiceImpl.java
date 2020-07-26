package com.superdevs.etlloader.service;

import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.model.CSVItem;
import com.superdevs.etlloader.util.DateUtilFormatter;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class CSVServiceImpl implements CSVService {

    private MongoTemplate mongoTemplate;

    private MapperFacade mapper;

    private DateUtilFormatter dateUtilFormatter;

    @Override
    public void saveAllCSVItems(List<CSVItem> items) {
        items.forEach(item -> mongoTemplate.save(item));
    }

    @Override
    public List<CSVItemDto> getAllItemsForWorkId(String uuid) {
        Query query = Query.query(Criteria.where("uuid").is(uuid));
        List<CSVItem> csvItems = mongoTemplate.find(query, CSVItem.class);

        List<CSVItemDto> csvItemDtos = mapper.mapAsList(csvItems, CSVItemDto.class);
        return csvItemDtos;
    }

    @Override
    public List<CSVItemDto> countTotalClicks(String datasource, String from, String to) {

        ZonedDateTime fromDate = dateUtilFormatter.convertDateFromString(from);
        ZonedDateTime tillDate = dateUtilFormatter.convertDateFromString(to);

        Criteria criteria = Criteria.where("dataSource").is(datasource)
                .andOperator(Criteria.where("daily")
                        .gt(fromDate)
                        .lte(tillDate));


        Query query = Query.query(criteria);
        List<CSVItem> csvItems = mongoTemplate.find(query, CSVItem.class);
        List<CSVItemDto> csvItemDtos = mapper.mapAsList(csvItems, CSVItemDto.class);
        return csvItemDtos;
    }
}
