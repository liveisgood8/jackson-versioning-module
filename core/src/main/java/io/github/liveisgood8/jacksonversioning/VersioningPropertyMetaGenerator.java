package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.Converter;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersioned;
import io.github.liveisgood8.jacksonversioning.annotation.JsonVersionedCollection;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VersioningPropertyMetaGenerator {

    public static VersioningPropertyMetaCollection forProperty(BeanProperty property) {
        JsonVersionedCollection jsonVersionedCollection = property.getAnnotation(JsonVersionedCollection.class);
        if (jsonVersionedCollection != null) {
            return fromAnnotation(jsonVersionedCollection);
        }

        JsonVersioned jsonVersioned = property.getAnnotation(JsonVersioned.class);
        if (jsonVersioned != null) {
            return VersioningPropertyMetaCollection.of(List.of(fromAnnotation(jsonVersioned)));
        }

        return VersioningPropertyMetaCollection.empty();
    }

    public static VersioningPropertyMetaCollection forPropertyDefinition(BeanPropertyDefinition propertyDefinition) {
        JsonVersionedCollection jsonVersionedCollection = getAnnotation(
                propertyDefinition,
                JsonVersionedCollection.class
        );
        if (jsonVersionedCollection != null) {
            return fromAnnotation(jsonVersionedCollection);
        }

        JsonVersioned jsonVersioned = getAnnotation(propertyDefinition, JsonVersioned.class);
        if (jsonVersioned != null) {
            return VersioningPropertyMetaCollection.of(List.of(fromAnnotation(jsonVersioned)));
        }

        return VersioningPropertyMetaCollection.empty();
    }

    private static <T extends BeanPropertyDefinition, A extends Annotation> A getAnnotation(
            T property,
            Class<A> clazz
    ) {
        A foundClazz = null;
        if (property.hasGetter()) {
            foundClazz = property.getGetter().getAnnotation(clazz);
        }
        if (property.hasField() && foundClazz == null) {
            foundClazz = property.getField().getAnnotation(clazz);
        }
        return foundClazz;
    }

    private static VersioningPropertyMetaCollection fromAnnotation(JsonVersionedCollection jsonVersionedCollection) {
        List<DefaultVersioningPropertyMeta> metaList = Arrays.stream(jsonVersionedCollection.value())
                .map(VersioningPropertyMetaGenerator::fromAnnotation)
                .collect(Collectors.toList());
        return VersioningPropertyMetaCollection.of(metaList);
    }

    private static DefaultVersioningPropertyMeta fromAnnotation(JsonVersioned jsonVersioned) {
        if (jsonVersioned == null) {
            return DefaultVersioningPropertyMeta.empty();
        }

        Version sinceVersion = getVersion(jsonVersioned.since());
        Version untilVersion = getVersion(jsonVersioned.until());
        Class<? extends Converter<?, ?>> converterClazz = jsonVersioned.converter();
        Class<? extends JsonSerializer<?>> serializerClazz = jsonVersioned.serializer();

        DefaultVersioningPropertyMeta meta;
        if (sinceVersion == null && untilVersion == null) {
            if (converterClazz != null || serializerClazz != null) {
                throw new IllegalArgumentException("Converter or serializer should be used in pair with since or until");
            }
            meta = DefaultVersioningPropertyMeta.empty();
        } else {
            meta = DefaultVersioningPropertyMeta.of(sinceVersion, untilVersion);
        }

        if (!jsonVersioned.name().isBlank()) {
            meta.setName(jsonVersioned.name());
        }
        if (!Objects.equals(serializerClazz, JsonSerializer.None.class)) {
            meta.setSerializerClass(serializerClazz);
        }
        if (!Objects.equals(converterClazz, Converter.None.class)) {
            meta.setConverterClazz(converterClazz);
        }

        return meta;
    }

    private static Version getVersion(String versionString) {
        if (versionString == null || versionString.isBlank()) {
            return null;
        }

        return Version.fromString(versionString);
    }
}
