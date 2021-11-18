package com.springboot.restexample;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;

public class ExampleDto {

    @JsonVersioned(since = "v2.0.0")
    private String name;

    @JsonVersioned(until = "v3.0.0", converter = ExampleConverter.class)
    @JsonVersioned(since = "v3.0.0")
    private String surname;

    private static class ExampleConverter extends StdConverter<String, String> {

        @Override
        public String convert(String surname) {
            return "Superhero";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
