package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.util.TestUtils;
import io.github.liveisgood8.jacksonversioning.util.VersionConstant;
import org.junit.jupiter.api.Test;

public class SamePropertyWithDifferentTypeTest {

    private static final TestObject TEST_OBJECT = new TestObject("500", 200);

    @Test
    void testForV1_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(
                String.format("{\"amount\":\"%s\"}", TEST_OBJECT.amountLegacy),
                TEST_OBJECT,
                VersionConstant.V1_0
        );
    }

    @Test
    void testForV2_0() throws JsonProcessingException {
        TestUtils.assertSerializedJson(
                String.format("{\"amount\":{\"rub\":%d,\"usd\":%d}}", TEST_OBJECT.amount.rub, TEST_OBJECT.amount.usd),
                TEST_OBJECT,
                VersionConstant.V2_0
        );
    }

    private static class TestObject {
        @JsonVersioned(until = VersionConstant.V2_0_STRING, name = "amount")
        public final String amountLegacy;

        @JsonVersioned(since = VersionConstant.V2_0_STRING)
        public final Amount amount;

        public TestObject(String amountLegacy, Integer amount) {
            this.amountLegacy = amountLegacy;
            this.amount = new Amount(amount, amount);
        }
    }

    private static class Amount {
        public final Integer rub;
        public final Integer usd;

        private Amount(Integer rub, Integer usd) {
            this.rub = rub;
            this.usd = usd;
        }
    }
}
