package io.github.liveisgood8.jacksonversioning.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.liveisgood8.jacksonversioning.Version;

import static io.github.liveisgood8.jacksonversioning.util.ObjectMapperFactory.createObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestUtils {

    public static void assertSerializedJson(String expectedJson, Object actualObject, Version version)
            throws JsonProcessingException {
        ObjectMapper mapper = createObjectMapper(version);

        String json = mapper.writeValueAsString(actualObject);

        assertEquals(expectedJson, json);
    }
}
