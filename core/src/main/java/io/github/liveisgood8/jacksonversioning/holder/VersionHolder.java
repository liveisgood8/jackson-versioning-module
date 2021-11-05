package io.github.liveisgood8.jacksonversioning.holder;

import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Contains information about currently used version.
 */
public interface VersionHolder {

    /**
     * Return held version.
     *
     * @return Held version.
     */
    Version getVersion();
}
