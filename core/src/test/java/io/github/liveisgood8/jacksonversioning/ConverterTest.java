package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.StdConverter;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static io.github.liveisgood8.jacksonversioning.util.TestUtils.assertSerializedJson;

class ConverterTest {

    private static final TestObject TEST_OBJECT = new TestObject("650");

    private static final Integer FIRST_VALUE = 756767;

    private static final Double SECOND_VALUE = 324325.1;

    @Test
    void testForV1_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"name\":\"%s\"}", TEST_OBJECT.name),
                TEST_OBJECT,
                VersionConstant.V1_0
        );
    }

    @Test
    void testForV2_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"name\":%d}", FIRST_VALUE),
                TEST_OBJECT,
                VersionConstant.V2_0
        );
    }

    @Test
    void testForV3_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format(Locale.US, "{\"name\":%.1f}", SECOND_VALUE),
                TEST_OBJECT,
                VersionConstant.V3_0
        );
    }

    private static class TestObject {
        @JsonVersioned(until = VersionConstant.V2_0_STRING)
        @JsonVersioned(
                since = VersionConstant.V2_0_STRING,
                until = VersionConstant.V3_0_STRING,
                converter = FirstConverter.class
        )
        @JsonVersioned(
                since = VersionConstant.V3_0_STRING,
                until = VersionConstant.V4_0_STRING,
                converter = SecondConverter.class
        )
        public final String name;

        public TestObject(String name) {
            this.name = name;
        }

        private static class FirstConverter extends StdConverter<String, Integer> {

            @Override
            public Integer convert(String value) {
                return FIRST_VALUE;
            }
        }

        private static class SecondConverter extends StdConverter<String, Double> {

            @Override
            public Double convert(String value) {
                return SECOND_VALUE;
            }
        }
    }
}
