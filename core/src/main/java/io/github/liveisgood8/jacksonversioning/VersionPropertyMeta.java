package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.Converter;

public interface VersionPropertyMeta {

    String getName();

    Class<? extends Converter<?, ?>> getConverterClass();

    Class<? extends JsonSerializer<?>> getSerializerClass();

    boolean isWithin(Version version);
}
