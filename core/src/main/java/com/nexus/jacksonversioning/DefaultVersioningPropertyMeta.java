package com.nexus.jacksonversioning;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public class DefaultVersioningPropertyMeta implements VersionPropertyMeta {

    private static final DefaultVersioningPropertyMeta EMPTY = new DefaultVersioningPropertyMeta();

    private final Version since;

    private final Version until;

    private String name = null;

    private Class<? extends JsonSerializer<?>> serializerClass = null;

    private Class<? extends Converter<?, ?>> converterClazz = null;

    public static DefaultVersioningPropertyMeta of(Version since, Version until) {
        return new DefaultVersioningPropertyMeta(since, until);
    }

    public static DefaultVersioningPropertyMeta empty() {
        return EMPTY;
    }

    private DefaultVersioningPropertyMeta() {
        this.since = null;
        this.until = null;
        this.converterClazz = null;
    }

    private DefaultVersioningPropertyMeta(Version since, Version until) {
        this.since = since;
        this.until = until;
        this.converterClazz = null;

        validateVersions();
    }

    private DefaultVersioningPropertyMeta(
            Version since,
            Version until,
            Class<? extends Converter<?, ?>> converterClazz
    ) {
        this.since = since;
        this.until = until;
        this.converterClazz = converterClazz;

        validateVersions();
    }

    private void validateVersions() {
        if (since == null || until == null) {
            return;
        }
        if (since.greaterThan(until) || since.equals(until)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Since version (%s) should be less than until version (%s)",
                            since,
                            until
                    )
            );
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerializerClass(Class<? extends JsonSerializer<?>> serializerClass) {
        this.serializerClass = serializerClass;
    }

    public void setConverterClazz(Class<? extends Converter<?, ?>> converterClazz) {
        this.converterClazz = converterClazz;
    }

    public String getName() {
        return name;
    }

    @Override
    public Class<? extends JsonSerializer<?>> getSerializerClass() {
        return serializerClass;
    }

    @Override
    public Class<? extends Converter<?, ?>> getConverterClass() {
        return converterClazz;
    }

    public boolean isWithin(Version version) {
        if (since == null && until == null) {
            return true;
        }

        if (since != null && until != null) {
            return version.compareTo(since) >= 0 && version.compareTo(until) < 0;
        }

        if (until != null && version.compareTo(until) < 0) {
            return true;
        }

        return since != null && version.compareTo(since) >= 0;
    }
}