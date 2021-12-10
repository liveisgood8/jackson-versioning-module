package io.github.liveisgood8.jacksonversioning.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.liveisgood8.jacksonversioning.JsonVersioningModule;
import io.github.liveisgood8.jacksonversioning.Version;
import io.github.liveisgood8.jacksonversioning.holder.serialize.SimpleVersionHolder;

public class ObjectMapperFactory {

    public static ObjectMapper createObjectMapper(Version version) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonVersioningModule(new SimpleVersionHolder(version)));

        return objectMapper;
    }
}
