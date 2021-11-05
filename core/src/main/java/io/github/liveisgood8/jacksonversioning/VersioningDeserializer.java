package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import io.github.liveisgood8.jacksonversioning.holder.VersionHolder;
import java.io.IOException;
import java.util.Optional;

public class VersioningDeserializer extends BeanDeserializer {

    private final VersionHolder versionHolder;

    private final BeanDescription beanDescription;

    private final BeanDeserializer deserializer;

    public VersioningDeserializer(
            VersionHolder versionHolder,
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
        Version version = versionHolder.getVersion();
        ObjectNode jsonNode = jsonParser.readValueAsTree();
        Optional<BeanPropertyDefinition> propertyNotInVersion = beanDescription.findProperties()
                .stream()
                .filter(
                        beanPropertyDefinition -> !VersioningPropertyMetaGenerator
                                .forPropertyDefinition(beanPropertyDefinition)
                                .isWithin(version)
                )
                .filter(beanPropertyDefinition -> jsonNode.has(beanPropertyDefinition.getName()))
                .findFirst();

        if (propertyNotInVersion.isPresent()) {
            throw ctx.mappingException("Property \"%s\" is not in version %s",
                    propertyNotInVersion.get().getName(), version.toString()
            );
        }

        JsonParser postInterceptionParser = new TreeTraversingParser(jsonNode, jsonParser.getCodec());
        postInterceptionParser.nextToken();

        return deserializer.deserialize(postInterceptionParser, ctx);
    }
}
