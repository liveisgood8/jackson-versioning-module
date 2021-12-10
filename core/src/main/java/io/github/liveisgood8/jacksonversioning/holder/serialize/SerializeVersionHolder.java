package io.github.liveisgood8.jacksonversioning.holder.serialize;

import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Provide information about currently used version for serialization.
 */
public interface SerializeVersionHolder {

    /**
     * Return held version.
     *
     * @param serializedObject Object which will be serialized.
     *
     * @return Held version.
     */
    Version getVersion(Object serializedObject);
}
