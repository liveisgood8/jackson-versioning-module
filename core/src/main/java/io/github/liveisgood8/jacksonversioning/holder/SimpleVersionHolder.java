package io.github.liveisgood8.jacksonversioning.holder;

import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Simple holder which hold constant version.
 */
public class SimpleVersionHolder implements VersionHolder {

    private final Version version;

    public SimpleVersionHolder(Version version) {
        this.version = version;
    }

    @Override
    public Version getVersion() {
        return version;
    }
}
