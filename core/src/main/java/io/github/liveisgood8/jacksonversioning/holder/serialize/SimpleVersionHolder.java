package io.github.liveisgood8.jacksonversioning.holder.serialize;

import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Simple holder which hold constant version.
 */
public class SimpleVersionHolder implements SerializeVersionHolder {

    private final Version version;

    public SimpleVersionHolder(Version version) {
        this.version = version;
    }

    @Override
    public Version getVersion(Object serializedObject) {
        return version;
    }
}
