package com.superdevs.etlloader.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtlController {

    @GetMapping
    public String works() {
        return "IT WORKS!";
    }

    @GetMapping("/{input}")
    public String sayHello(@PathVariable String input) {
        return "SAY HELLO " + input + " !!!";
    }
}
