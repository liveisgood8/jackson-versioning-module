package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

import static io.github.liveisgood8.jacksonversioning.util.ObjectMapperFactory.createObjectMapper;
import static io.github.liveisgood8.jacksonversioning.util.TestUtils.assertSerializedJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SinceAndUntilCombinationTest {

    private static final TestObject TEST_OBJECT = new TestObject("test", "test2");

    private static final TestObjectWithInvalidVersionRange TEST_OBJECT_WITH_INVALID_VERSION_RANGE =
            new TestObjectWithInvalidVersionRange("test");

    @Test
    void testForV1_0() throws JsonProcessingException {
        assertSerializedJson("{}", TEST_OBJECT, VersionConstant.V1_0);
    }

    @Test
    void testForV2_0() throws JsonProcessingException {
        assertSerializedJson(String.format("{\"name\":\"%s\"}", TEST_OBJECT.name), TEST_OBJECT, VersionConstant.V2_0);
    }

    @Test
    void testForV4_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"surname\":\"%s\"}", TEST_OBJECT.surname),
                TEST_OBJECT,
                VersionConstant.V4_0
        );
    }

    @Test
    void testInvalidVersionRange() {
        var mapper = createObjectMapper(VersionConstant.V2_0);

        JsonMappingException exception = assertThrows(
                JsonMappingException.class,
                () -> mapper.writeValueAsString(TEST_OBJECT_WITH_INVALID_VERSION_RANGE)
        );

        assertEquals(
                "Since version (v4.0) should be less than until version (v1.0) " +
                        "(through reference chain: " +
                        "io.github.liveisgood8.jacksonversioning.SinceAndUntilCombinationTest$TestObjectWithInvalidVersionRange[\"name\"])",
                exception.getMessage()
        );
    }

    private static class TestObject {
        @JsonVersioned(since = VersionConstant.V2_0_STRING, until = VersionConstant.V3_0_STRING)
        public final String name;

        @JsonVersioned(since = VersionConstant.V4_0_STRING, until = VersionConstant.V5_0_STRING)
        public final String surname;

        public TestObject(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
    }

    private static class TestObjectWithInvalidVersionRange {
        @JsonVersioned(since = VersionConstant.V4_0_STRING, until = VersionConstant.V1_0_STRING)
        public final String name;

        public TestObjectWithInvalidVersionRange(String name) {
            this.name = name;
        }
    }
}
