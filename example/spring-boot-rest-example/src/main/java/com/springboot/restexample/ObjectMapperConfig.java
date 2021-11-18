package com.springboot.restexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.liveisgood8.jacksonversioning.JsonVersioningModule;
import io.github.liveisgood8.jacksonversioning.holder.InheritableThreadLocalVersionHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonVersioningModule(InheritableThreadLocalVersionHolder.get()));
        return objectMapper;
    }
}
