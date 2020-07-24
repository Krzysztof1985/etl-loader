package com.superdevs.etlloader.service;

import com.superdevs.etlloader.model.Animal;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private MongoTemplate mongoTemplate;

    @Override
    public Animal findAnimalByName(String name) {
        Query query = Query.query(Criteria.where("name").is(name));
        Animal animal = mongoTemplate.findOne(query, Animal.class);
        return animal;
    }
}
