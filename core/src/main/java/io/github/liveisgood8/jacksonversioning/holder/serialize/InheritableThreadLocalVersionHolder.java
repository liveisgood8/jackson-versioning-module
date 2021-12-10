package io.github.liveisgood8.jacksonversioning.holder.serialize;

import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Inheritable thread local version holder.
 */
public class InheritableThreadLocalVersionHolder implements SerializeVersionHolder {

    private final InheritableThreadLocal<Version> versionHolderThreadLocal = new InheritableThreadLocal<>();

    private static InheritableThreadLocalVersionHolder INSTANCE;

    private InheritableThreadLocalVersionHolder(Version version) {
        versionHolderThreadLocal.set(version);
    }

    /**
     * Initialize holder with specified version.
     *
     * @param version Version for hold.
     */
    public static void initialize(Version version) {
        if (INSTANCE == null) {
            INSTANCE = new InheritableThreadLocalVersionHolder(version);
        } else {
            INSTANCE.versionHolderThreadLocal.set(version);
        }
    }

    /**
     * Return instance of initialized holder.
     *
     * @return Instance of version holder.
     */
    public static InheritableThreadLocalVersionHolder get() {
        if (INSTANCE == null) {
            INSTANCE = new InheritableThreadLocalVersionHolder(Version.empty());
        }
        return INSTANCE;
    }

    @Override
    public Version getVersion(Object serializedObject) {
        return versionHolderThreadLocal.get();
    }
}
