package com.superdevs.etlloader.service;

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
    public List<Document> generateAggregatedData(MainFilter mainFilter) {
        Set<String> dataSources = mainFilter.getDataSource();
        String from = mainFilter.getFrom();
        String to = mainFilter.getTo();
        ZonedDateTime fromDate = dateUtilFormatter.convertDateFromString(from);
        ZonedDateTime tillDate = dateUtilFormatter.convertDateFromString(to);

        String[] sumFieldsToCalc = mainFilter.getMetrics()
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        Criteria matchCriteria = generateCriteria(mainFilter, dataSources,
                fromDate, tillDate);

        String dimension = mainFilter.getGroupDimension().name();

        GroupOperation groupOperation = Aggregation.group(dimension);
        groupOperation.addToSet(dimension).as(dimension);

        for (int i = 0; i < sumFieldsToCalc.length; i++) {
            String groupingFieldName = sumFieldsToCalc[i];
            groupOperation = groupOperation.sum(groupingFieldName).as(groupingFieldName);
        }
        MatchOperation match = Aggregation.match(matchCriteria);
        Aggregation aggregation = Aggregation.newAggregation(match, groupOperation);
        /**
         * private GroupOperationBuilder newBuilder(Keyword keyword, @Nullable String reference, @Nullable Object value) {
         * 		return new GroupOperationBuilder(this, new Operation(keyword, null, reference, value));
         *        }
         */
        AggregationResults<Document> aggregate = mongoTemplate.aggregate(aggregation, CSVItem.class, Document.class);

        List<Document> mappedResults = aggregate.getMappedResults();
        return mappedResults;
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
