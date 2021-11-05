package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class CustomBeanPropertyWriter extends BeanPropertyWriter {

    public CustomBeanPropertyWriter(BeanPropertyWriter base, JsonSerializer<?> serializer) {
        super(base);
        this._serializer = (JsonSerializer<Object>) serializer;
    }

    public CustomBeanPropertyWriter(BeanPropertyWriter base, SerializedString name) {
        super(base, name);
    }
}
