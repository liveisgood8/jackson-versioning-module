package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.util.TestUtils;
import io.github.liveisgood8.jacksonversioning.util.VersionConstant;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class SerializerTest {

    private static final TestObject TEST_OBJECT = new TestObject("650");

    private static final String FIRST_VALUE = "3534";

    private static final String SECOND_VALUE = "8000";

    @Test
    void testForV1_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(
                String.format("{\"name\":\"%s\"}", TEST_OBJECT.name),
                TEST_OBJECT,
                VersionConstant.V1_0
        );
    }

    @Test
    void testForV2_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(
                String.format("{\"name\":\"%s\"}", FIRST_VALUE),
                TEST_OBJECT,
                VersionConstant.V2_0
        );
    }

    @Test
    void testForV3_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(
                String.format("{\"name\":\"%s\"}", SECOND_VALUE),
                TEST_OBJECT,
                VersionConstant.V3_0
        );
    }

    private static class TestObject {
        @JsonVersioned(until = VersionConstant.V2_0_STRING)
        @JsonVersioned(
                since = VersionConstant.V2_0_STRING,
                until = VersionConstant.V3_0_STRING,
                serializer = FirstSerializer.class
        )
        @JsonVersioned(
                since = VersionConstant.V3_0_STRING,
                until = VersionConstant.V4_0_STRING,
                serializer = SecondSerializer.class
        )
        public final String name;

        public TestObject(String name) {
            this.name = name;
        }

        public static class FirstSerializer extends JsonSerializer<String> {

            @Override
            public void serialize(
                    String value,
                    JsonGenerator gen,
                    SerializerProvider provider
            ) throws IOException {
                gen.writeString(FIRST_VALUE);
            }
        }

        public static class SecondSerializer extends JsonSerializer<String> {

            @Override
            public void serialize(
                    String value,
                    JsonGenerator gen,
                    SerializerProvider provider
            ) throws IOException {
                gen.writeString(SECOND_VALUE);
            }
        }
    }
}
