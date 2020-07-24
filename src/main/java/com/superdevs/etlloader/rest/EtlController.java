package com.superdevs.etlloader.rest;

import com.superdevs.etlloader.model.Animal;
import com.superdevs.etlloader.repository.AnimalRepo;
import com.superdevs.etlloader.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class EtlController {

    private AnimalService service;
    private AnimalRepo animalRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        animalRepo.deleteAll();
        Animal animal1 = new Animal("Dog", 3);
        Animal animal2 = new Animal("Cat", 23);
        Animal animal3 = new Animal("Mouse", 1);
        Animal animal4 = new Animal("Bull", 4);
        animalRepo.save(animal1);
        animalRepo.save(animal2);
        animalRepo.save(animal3);
        animalRepo.save(animal4);
    }

    @GetMapping
    public String works() {
        return "IT WORKS!";
    }

    @GetMapping("hello/{input}")
    public String sayHello(@PathVariable String input) {
        return "SAY HELLO " + input;
    }

    @GetMapping("/getAll")
    public List<Animal> getAll() {
        return animalRepo.findAll();
    }

    @GetMapping("find/{name}")
    public Animal findByName(@PathVariable String name) {
       return service.findAnimalByName(name);
    }


}
