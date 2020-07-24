package com.superdevs.etlloader.repository;

import com.superdevs.etlloader.model.Animal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepo extends MongoRepository<Animal, String> {
}
