package com.nexus.jacksonversioning.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nexus.jacksonversioning.Version;

import static com.nexus.jacksonversioning.util.ObjectMapperFactory.createObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestUtils {

    public static void assertSerializedJson(String expectedJson, Object actualObject, Version version)
            throws JsonProcessingException {
        var mapper = createObjectMapper(version);

        String json = mapper.writeValueAsString(actualObject);

        assertEquals(expectedJson, json);
    }
}
