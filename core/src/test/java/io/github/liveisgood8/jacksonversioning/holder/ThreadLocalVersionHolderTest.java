package io.github.liveisgood8.jacksonversioning.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.liveisgood8.jacksonversioning.Version;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadLocalVersionHolderTest {

    private final Version version = Version.of(1324, 5);

    private final Version localVersion = Version.of(345465, 435);

    @Test
    void testGetVersionInSameThread() {
        ThreadLocalVersionHolder.initialize(version);

        assertEquals(version, ThreadLocalVersionHolder.get().getVersion());
    }

    @Test
    void testGetVersionInSameThreadWhenNotInitialized() {
        assertEquals(Version.empty(), ThreadLocalVersionHolder.get().getVersion());
    }

    @Test
    void testGetVersionInAnotherThreadWhenInitializedInsideThread() {
        ThreadLocalVersionHolder.initialize(version);

        CompletableFuture.runAsync(() -> {
            ThreadLocalVersionHolder.initialize(localVersion);
            assertEquals(localVersion, ThreadLocalVersionHolder.get().getVersion());
        }).join();
    }

    @Test
    void testGetVersionWhenSwitched() {
        Version first = Version.of(123, 123);
        Version second = Version.of(546546, 1);

        VersionHolder versionHolder = ThreadLocalVersionHolder.get();

        ThreadLocalVersionHolder.initialize(first);
        assertEquals(first, versionHolder.getVersion());

        ThreadLocalVersionHolder.initialize(second);
        assertEquals(second, versionHolder.getVersion());
    }
}