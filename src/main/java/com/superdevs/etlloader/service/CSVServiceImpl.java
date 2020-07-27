package com.superdevs.etlloader.service;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.filters.MainFilter;
import com.superdevs.etlloader.model.CSVItem;
import com.superdevs.etlloader.util.DateUtilFormatter;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
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
    public void deleteAllItems() {
        mongoTemplate.remove(new Query(), CSVItem.class);
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
    public List<Document> generateAggregatedData(MainFilter mainFilter) {
        Set<String> dataSources = mainFilter.getDataSource();
        String from = mainFilter.getFrom();
        String to = mainFilter.getTo();
        ZonedDateTime fromDate = dateUtilFormatter.convertDateFromString(from);
        ZonedDateTime tillDate = dateUtilFormatter.convertDateFromString(to);

        String[] groupingFields = mainFilter.getMetrics()
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        Criteria matchCriteria = generateCriteria(mainFilter, dataSources,
                fromDate, tillDate);

        GroupOperation groupOperation = Aggregation.group(groupingFields);
        for (int i = 0; i < groupingFields.length; i++) {
            String groupingFieldName = groupingFields[i];
            groupOperation = groupOperation.sum(groupingFieldName).as(groupingFieldName);
        }

//        List<Bson> bsons = Arrays.asList(Aggregates.match(Filters.and(Filters.in("dataSource", Arrays.asList("Google Ads", "Twitter Ads")), Filters.gte("clicks", 100L), Filters.eq("campaign", "Adventmarkt Touristik"))), Aggregates.group("$dataSource", Accumulators.sum("totalClicks", "$clicks"),
//                Accumulators.sum("totalImpressions", "$impressions")));

        AccumulatorOperators.Sum clicks = AccumulatorOperators.Sum.sumOf("clicks");



        AggregationUpdate.group("_id")
                .addToSet(name)
                .as("groupedBy")
                .sum("clicks");


        MatchOperation match = Aggregation.match(matchCriteria);
        Aggregation aggregation = AggregationUpdate.newAggregation(match, groupOperation);
//
        AggregationResults<Document> aggregate = mongoTemplate.aggregate(aggregation, CSVItem.class, Document.class);
//
//        List<Document> mappedResults = aggregate.getMappedResults();
//        return mappedResults;
        return null;
    }

    private Criteria generateCriteria(MainFilter mainFilter, Set<String> dataSources, ZonedDateTime fromDate, ZonedDateTime tillDate) {
        Criteria matchCriteria = null;
        Set<String> campaigns = mainFilter.getCampaigns();
        if (campaigns.size() > 0) {
            matchCriteria = Criteria.where("dataSource").in(dataSources)
                    .andOperator(Criteria.where("daily")
                            .gt(fromDate)
                            .lte(tillDate))
                    .and("campaign").in(campaigns);
        } else {
            matchCriteria = Criteria.where("dataSource").in(dataSources)
                    .andOperator(Criteria.where("daily")
                            .gt(fromDate)
                            .lte(tillDate));
        }
        return matchCriteria;
    }
}
