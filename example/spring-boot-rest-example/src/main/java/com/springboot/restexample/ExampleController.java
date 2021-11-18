package com.springboot.restexample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController {

    @GetMapping
    public ExampleDto getExample() {
        ExampleDto exampleDto = new ExampleDto();
        exampleDto.setName("Alex");
        exampleDto.setSurname("Clarke");
        return exampleDto;
    }
}
