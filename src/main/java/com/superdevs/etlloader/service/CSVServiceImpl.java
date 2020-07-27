package com.superdevs.etlloader.service;

import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.filters.MainFilter;
import com.superdevs.etlloader.model.CSVItem;
import com.superdevs.etlloader.util.DateUtilFormatter;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

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

    @Override
    public void generateAggregatedData(MainFilter mainFilter) {

        Set<String> dataSources = mainFilter.getDataSource();


        String from = mainFilter.getFrom();
        String to = mainFilter.getTo();
        ZonedDateTime fromDate = dateUtilFormatter.convertDateFromString(from);
        ZonedDateTime tillDate = dateUtilFormatter.convertDateFromString(to);


        String[] groupingFields = mainFilter.getMetrics()
                .entrySet()
                .stream()
                .filter(e -> e.getValue())
                .map(e -> e.getKey())
                .toArray(String[]::new);

        Criteria matchCriteria = Criteria.where("dataSource").in(dataSources)
                .andOperator(Criteria.where("daily")
                        .gt(fromDate)
                        .lte(tillDate));

        GroupOperation groupOperation = Aggregation.group(groupingFields);

        MatchOperation match = Aggregation.match(matchCriteria);

        Aggregation aggregation = Aggregation.newAggregation(match, groupOperation);

        AggregationResults<Document> aggregate = mongoTemplate.aggregate(aggregation, CSVItem.class, Document.class);


        List<Document> mappedResults = aggregate.getMappedResults();
        System.out.println(mappedResults);

    }
}
