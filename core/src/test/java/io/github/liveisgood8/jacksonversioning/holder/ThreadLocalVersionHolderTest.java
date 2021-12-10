package io.github.liveisgood8.jacksonversioning.holder;

import io.github.liveisgood8.jacksonversioning.Version;
import java.util.concurrent.CompletableFuture;

import io.github.liveisgood8.jacksonversioning.holder.serialize.ThreadLocalVersionHolder;
import io.github.liveisgood8.jacksonversioning.holder.serialize.SerializeVersionHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadLocalVersionHolderTest {

    private final Version version = Version.of(1324, 5);

    private final Version localVersion = Version.of(345465, 435);

    @Test
    void testGetVersionInSameThread() {
        ThreadLocalVersionHolder.initialize(version);

        assertEquals(version, ThreadLocalVersionHolder.get().getVersion(new Object()));
    }

    @Test
    void testGetVersionInSameThreadWhenNotInitialized() {
        assertEquals(Version.empty(), ThreadLocalVersionHolder.get().getVersion(new Object()));
    }

    @Test
    void testGetVersionInAnotherThreadWhenInitializedInsideThread() {
        ThreadLocalVersionHolder.initialize(version);

        CompletableFuture.runAsync(() -> {
            ThreadLocalVersionHolder.initialize(localVersion);
            assertEquals(localVersion, ThreadLocalVersionHolder.get().getVersion(new Object()));
        }).join();
    }

    @Test
    void testGetVersionWhenSwitched() {
        Version first = Version.of(123, 123);
        Version second = Version.of(546546, 1);

        SerializeVersionHolder versionHolder = ThreadLocalVersionHolder.get();

        ThreadLocalVersionHolder.initialize(first);
        assertEquals(first, versionHolder.getVersion(new Object()));

        ThreadLocalVersionHolder.initialize(second);
        assertEquals(second, versionHolder.getVersion(new Object()));
    }
}