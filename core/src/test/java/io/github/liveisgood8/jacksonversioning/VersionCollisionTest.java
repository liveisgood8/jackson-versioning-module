package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

import static io.github.liveisgood8.jacksonversioning.util.ObjectMapperFactory.createObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VersionCollisionTest {

    @Test
    void testFirstTestObject() {
        ObjectMapper mapper = createObjectMapper(VersionConstant.V3_0);

        JsonMappingException exception = assertThrows(
                JsonMappingException.class,
                () -> mapper.writeValueAsString(new FirstTestObject())
        );

        assertEquals(
                "For version: v3.0 founded multiple definitions: since v2.0,since v3.0 " +
                        "(through reference chain: " +
                        "io.github.liveisgood8.jacksonversioning.VersionCollisionTest$FirstTestObject[\"amount\"])",
                exception.getMessage()
        );
    }

    @Test
    void testSecondTestObject() {
        ObjectMapper mapper = createObjectMapper(VersionConstant.V3_0);

        JsonMappingException exception = assertThrows(
                JsonMappingException.class,
                () -> mapper.writeValueAsString(new SecondTestObject())
        );

        assertEquals(
                "For version: v3.0 founded multiple definitions: since v2.0 - until v5.0,since v3.0 - until v4.0 " +
                        "(through reference chain: " +
                        "io.github.liveisgood8.jacksonversioning.VersionCollisionTest$SecondTestObject[\"amount\"])",
                exception.getMessage()
        );
    }

    @Test
    void testThirdTestObject() {
        ObjectMapper mapper = createObjectMapper(VersionConstant.V3_0);

        JsonMappingException exception = assertThrows(
                JsonMappingException.class,
                () -> mapper.writeValueAsString(new ThirdTestObject())
        );

        assertEquals(
                "For version: v3.0 founded multiple definitions: since v2.0 - until v5.0,since v2.0 - until v4.0 " +
                        "(through reference chain: " +
                        "io.github.liveisgood8.jacksonversioning.VersionCollisionTest$ThirdTestObject[\"amount\"])",
                exception.getMessage()
        );
    }

    @Test
    void testFourthTestObject() {
        ObjectMapper mapper = createObjectMapper(VersionConstant.V4_0);

        JsonMappingException exception = assertThrows(
                JsonMappingException.class,
                () -> mapper.writeValueAsString(new FourthTestObject())
        );

        assertEquals(
                "For version: v4.0 founded multiple definitions: since v2.0 - until v5.0,since v4.0 - until v5.0 " +
                        "(through reference chain: " +
                        "io.github.liveisgood8.jacksonversioning.VersionCollisionTest$FourthTestObject[\"amount\"])",
                exception.getMessage()
        );
    }

    private static class FirstTestObject {

        @JsonVersioned(since = VersionConstant.V2_0_STRING)
        @JsonVersioned(since = VersionConstant.V3_0_STRING)
        public final Integer amount = 300;
    }

    private static class SecondTestObject {

        @JsonVersioned(since = VersionConstant.V2_0_STRING, until = VersionConstant.V5_0_STRING)
        @JsonVersioned(since = VersionConstant.V3_0_STRING, until = VersionConstant.V4_0_STRING)
        public final Integer amount = 300;
    }

    private static class ThirdTestObject {

        @JsonVersioned(since = VersionConstant.V2_0_STRING, until = VersionConstant.V5_0_STRING)
        @JsonVersioned(since = VersionConstant.V2_0_STRING, until = VersionConstant.V4_0_STRING)
        public final Integer amount = 300;
    }

    private static class FourthTestObject {

        @JsonVersioned(since = VersionConstant.V2_0_STRING, until = VersionConstant.V5_0_STRING)
        @JsonVersioned(since = VersionConstant.V4_0_STRING, until = VersionConstant.V5_0_STRING)
        public final Integer amount = 300;
    }
}
