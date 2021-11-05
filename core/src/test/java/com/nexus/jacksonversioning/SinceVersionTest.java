package com.nexus.jacksonversioning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nexus.jacksonversioning.annotation.JsonVersioned;
import com.nexus.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

import static com.nexus.jacksonversioning.util.TestUtils.assertSerializedJson;

class SinceVersionTest {

    private static final TestObject TEST_OBJECT = new TestObject("test", 232);

    @Test
    void testForV1_0() throws JsonProcessingException {
        assertSerializedJson(String.format("{\"name\":\"%s\"}", TEST_OBJECT.name), TEST_OBJECT, VersionConstant.V1_0);
    }

    @Test
    void testForV2_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"name\":\"%s\",\"amount_v2\":%d}", TEST_OBJECT.name, TEST_OBJECT.amount),
                TEST_OBJECT,
                VersionConstant.V2_0
        );
    }

    @Test
    void testForV3_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"name\":\"%s\",\"amount_v3\":%d}", TEST_OBJECT.name, TEST_OBJECT.amount),
                TEST_OBJECT,
                VersionConstant.V3_0
        );
    }

    private static class TestObject {
        public final String name;

        @JsonVersioned(since = VersionConstant.V2_0_STRING, until = VersionConstant.V3_0_STRING, name = "amount_v2")
        @JsonVersioned(since = VersionConstant.V3_0_STRING, name = "amount_v3")
        public final Integer amount;

        public TestObject(String name, Integer amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}
