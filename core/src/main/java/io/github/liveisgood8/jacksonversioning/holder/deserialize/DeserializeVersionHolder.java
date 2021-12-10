package io.github.liveisgood8.jacksonversioning.holder.deserialize;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.liveisgood8.jacksonversioning.Version;

/**
 * Provide information about currently used version for deserialization.
 */
public interface DeserializeVersionHolder {

    /**
     * Return held version.
     *
     * @param deserializedJsonRootNode Root node of deserialized json.
     *
     * @return Held version.
     */
    Version getVersion(JsonNode deserializedJsonRootNode);
}
