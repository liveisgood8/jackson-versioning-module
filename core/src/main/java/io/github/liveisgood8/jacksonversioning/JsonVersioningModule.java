package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import io.github.liveisgood8.jacksonversioning.holder.VersionHolder;

public class JsonVersioningModule extends SimpleModule {

    public JsonVersioningModule(VersionHolder versionHolder) {
        setDeserializerModifier(
                new BeanDeserializerModifier() {
                    @Override
                    public JsonDeserializer<?> modifyDeserializer(
                            DeserializationConfig config,
                            BeanDescription beanDescription,
                            JsonDeserializer<?> deserializer
                    ) {
                        // TODO Implement deserializer
                        /*if (deserializer instanceof BeanDeserializer) {
                            return new VersioningDeserializer(
                                    versionHolder,
                                    beanDescription,
                                    (BeanDeserializer) deserializer
                            );
                        }*/
                        return super.modifyDeserializer(config, beanDescription, deserializer);
                    }
                }
        );

        setSerializerModifier(
                new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(
                            SerializationConfig config,
                            BeanDescription beanDescription,
                            JsonSerializer<?> serializer
                    ) {
                        if (serializer instanceof BeanSerializer) {
                            return new VersioningSerializer(versionHolder, (BeanSerializerBase) serializer);
                        }
                        return super.modifySerializer(config, beanDescription, serializer);
                    }
                }
        );
    }

    // TODO Implement custom annotation instrospector in future
    //@Override
    //public void setupModule(SetupContext context) {
    //    super.setupModule(context);
    //
    //    context.insertAnnotationIntrospector(new VersioningAnnotationInstrospector());
    //}
}
