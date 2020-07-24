package com.superdevs.etlloader.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

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
        MongoTemplate template = new MongoTemplate(mongoDbFactory());
        return template;
    }
}
