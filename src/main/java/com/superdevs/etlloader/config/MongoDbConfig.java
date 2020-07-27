package com.superdevs.etlloader.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.superdevs.etlloader.config.converters.DateToZonedDateTimeConverter;
import com.superdevs.etlloader.config.converters.ZonedDateTimeToDateConverter;
import com.superdevs.etlloader.config.converters.to.document.DocumentToZonedDateTimeConverter;
import com.superdevs.etlloader.config.converters.to.document.ZonedDateTimeWriteConverter;
import com.superdevs.etlloader.model.CSVItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Configuration
public class MongoDbConfig {

    @Value("${spring.mongodb.uri}")
    private String connectionString;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connString = new ConnectionString(connectionString);
        MongoClient mongoClient = MongoClients.create(connString);
        return mongoClient;
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient(), "csv_loader");
        return factory;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDbFactory()), new MongoMappingContext());
        converter.setCustomConversions(customConversions());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.afterPropertiesSet();
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);

        //due to some issues with this spring version , CompoundIndexes are not working properly!
        Index indexDef = new Index()
                .on("dataSource", ASC)
                .on("campaign", ASC)
                .on("daily", ASC)
                .on("clicks", ASC)
                .on("impressions", ASC)
                .unique();

        mongoTemplate.indexOps(CSVItem.class)
                .ensureIndex(indexDef);

        return mongoTemplate;
    }

    /*
     * Mongo has a problem with ZonedDateTime,
     * and Integer/Long mapping read/write operations
     */
    @Bean
    public CustomConversions customConversions() {
        List<Converter<?,?>> converters = new ArrayList<Converter<?,?>>();
        converters.add(new MongoConverters.BigDecimalDecimal128Converter());
        converters.add(new MongoConverters.Decimal128BigDecimalConverter());
        converters.add(new DateToZonedDateTimeConverter());
        converters.add(new DocumentToZonedDateTimeConverter());
        converters.add(new ZonedDateTimeToDateConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new CustomConversions(converters);
    }
}
