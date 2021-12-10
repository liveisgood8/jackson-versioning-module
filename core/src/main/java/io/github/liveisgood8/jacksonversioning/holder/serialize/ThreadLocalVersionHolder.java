package io.github.liveisgood8.jacksonversioning.holder.serialize;

import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Thread local version holder.
 */
public class ThreadLocalVersionHolder implements SerializeVersionHolder {

    private final ThreadLocal<Version> versionHolderThreadLocal = new ThreadLocal<>();

    private static ThreadLocalVersionHolder INSTANCE;

    private ThreadLocalVersionHolder(Version version) {
        versionHolderThreadLocal.set(version);
    }

    /**
     * Initialize holder with specified version.
     *
     * @param version Version for hold.
     */
    public static void initialize(Version version) {
        if (INSTANCE == null) {
            INSTANCE = new ThreadLocalVersionHolder(version);
        } else {
            INSTANCE.versionHolderThreadLocal.set(version);
        }
    }

    /**
     * Return instance of initialized holder.
     *
     * @return Instance of version holder.
     */
    public static ThreadLocalVersionHolder get() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadLocalVersionHolder(Version.empty());
        }
        return INSTANCE;
    }

    @Override
    public Version getVersion(Object serializedObject) {
        return versionHolderThreadLocal.get();
    }
}
