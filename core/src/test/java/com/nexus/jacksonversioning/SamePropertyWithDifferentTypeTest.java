package com.nexus.jacksonversioning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nexus.jacksonversioning.annotation.JsonVersioned;
import com.nexus.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

import static com.nexus.jacksonversioning.util.TestUtils.assertSerializedJson;

public class SamePropertyWithDifferentTypeTest {

    private static final TestObject TEST_OBJECT = new TestObject("500", 200);

    @Test
    void testForV1_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"amount\":\"%s\"}", TEST_OBJECT.amountLegacy),
                TEST_OBJECT,
                VersionConstant.V1_0
        );
    }

    @Test
    void testForV2_0() throws JsonProcessingException {
        assertSerializedJson(
                String.format("{\"amount\":%d}", TEST_OBJECT.amount),
                TEST_OBJECT,
                VersionConstant.V2_0
        );
    }

    private static class TestObject {
        @JsonVersioned(until = VersionConstant.V2_0_STRING, name = "amount")
        public final String amountLegacy;

        @JsonVersioned(since = VersionConstant.V2_0_STRING)
        public final Integer amount;

        public TestObject(String amountLegacy, Integer amount) {
            this.amountLegacy = amountLegacy;
            this.amount = amount;
        }
    }
}
