package io.github.liveisgood8.jacksonversioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.liveisgood8.jacksonversioning.JsonVersioningModule;
import io.github.liveisgood8.jacksonversioning.Version;

public class ObjectMapperFactory {

    public static ObjectMapper createObjectMapper(Version version) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonVersioningModule(version));

        return objectMapper;
    }
}
