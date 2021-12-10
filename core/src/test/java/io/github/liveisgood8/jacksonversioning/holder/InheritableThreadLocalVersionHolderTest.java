package io.github.liveisgood8.jacksonversioning.holder;

import io.github.liveisgood8.jacksonversioning.Version;
import java.util.concurrent.CompletableFuture;

import io.github.liveisgood8.jacksonversioning.holder.serialize.InheritableThreadLocalVersionHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InheritableThreadLocalVersionHolderTest {

    private final Version version = Version.of(1324, 5);

    @Test
    void testGetVersionInSameThreadWhenNotInitialized() {
        assertEquals(Version.empty(), InheritableThreadLocalVersionHolder.get().getVersion(new Object()));
    }

    @Test
    void testGetVersionInAnotherThreadWhenInheritable() {
        InheritableThreadLocalVersionHolder.initialize(version);

        CompletableFuture.runAsync(() -> {
            assertEquals(version, InheritableThreadLocalVersionHolder.get().getVersion(new Object()));
        }).join();
    }
}