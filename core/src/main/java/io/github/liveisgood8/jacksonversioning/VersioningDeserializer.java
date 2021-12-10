package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import io.github.liveisgood8.jacksonversioning.holder.deserialize.DeserializeVersionHolder;
import io.github.liveisgood8.jacksonversioning.holder.serialize.SerializeVersionHolder;

import java.io.IOException;
import java.util.Optional;

public class VersioningDeserializer extends BeanDeserializer {

    private final DeserializeVersionHolder versionHolder;

    private final BeanDescription beanDescription;

    private final BeanDeserializer deserializer;

    public VersioningDeserializer(
            DeserializeVersionHolder versionHolder,
            BeanDescription beanDescription,
            BeanDeserializer deserializer
    ) {
        super(deserializer);
        this.versionHolder = versionHolder;
        this.beanDescription = beanDescription;
        this.deserializer = deserializer;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        ObjectNode rootJsonNode = jsonParser.readValueAsTree();
        Version version = versionHolder.getVersion(rootJsonNode);

        Optional<BeanPropertyDefinition> propertyNotInVersion = beanDescription.findProperties()
                .stream()
                .filter(
                        beanPropertyDefinition -> !VersioningPropertyMetaGenerator
                                .forPropertyDefinition(beanPropertyDefinition)
                                .isWithin(version)
                )
                .filter(beanPropertyDefinition -> rootJsonNode.has(beanPropertyDefinition.getName()))
                .findFirst();

        if (propertyNotInVersion.isPresent()) {
            ctx.reportInputMismatch(
                    ctx.getContextualType(),
                    "Property \"%s\" is not in version %s",
                    propertyNotInVersion.get().getName(),
                    version.toString()
            );
        }

        JsonParser postInterceptionParser = new TreeTraversingParser(rootJsonNode, jsonParser.getCodec());
        postInterceptionParser.nextToken();

        return deserializer.deserialize(postInterceptionParser, ctx);
    }
}
