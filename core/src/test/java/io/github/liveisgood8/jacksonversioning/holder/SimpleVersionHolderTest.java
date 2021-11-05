package io.github.liveisgood8.jacksonversioning.holder;

import io.github.liveisgood8.jacksonversioning.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleVersionHolderTest {

    private final Version version = Version.of(1, 5);

    private final VersionHolder holder = new SimpleVersionHolder(version);

    @Test
    void testGetVersion() {
        assertEquals(version, holder.getVersion());
    }
}