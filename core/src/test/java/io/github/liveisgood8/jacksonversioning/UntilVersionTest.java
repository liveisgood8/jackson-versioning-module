package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.util.TestUtils;
import io.github.liveisgood8.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

class UntilVersionTest {

    private static final int AMOUNT = 500;

    private static final TestObject TEST_OBJECT = new TestObject("test");

    @Test
    void testForV1_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(
                String.format("{\"name\":\"%s\",\"amount\":%d}", TEST_OBJECT.name, AMOUNT),
                TEST_OBJECT,
                VersionConstant.V1_0
        );
    }

    @Test
    void testForV3_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(String.format("{\"name\":\"%s\"}", TEST_OBJECT.name), TEST_OBJECT, VersionConstant.V3_0);
    }

    private static class TestObject {
        public final String name;

        public TestObject(String name) {
            this.name = name;
        }

        @JsonVersioned(until = VersionConstant.V2_0_STRING)
        public Integer getAmount() {
            return AMOUNT;
        }
    }
}
