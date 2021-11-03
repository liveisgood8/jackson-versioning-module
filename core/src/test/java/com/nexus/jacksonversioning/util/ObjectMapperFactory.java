package com.nexus.jacksonversioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.jacksonversioning.JsonVersioningModule;
import com.nexus.jacksonversioning.Version;

public class ObjectMapperFactory {

    public static ObjectMapper createObjectMapper(Version version) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonVersioningModule(version));

        return objectMapper;
    }
}
