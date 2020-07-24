package com.superdevs.etlloader.service;

import com.superdevs.etlloader.model.Animal;

public interface AnimalService {

    Animal findAnimalByName(String name);
}
