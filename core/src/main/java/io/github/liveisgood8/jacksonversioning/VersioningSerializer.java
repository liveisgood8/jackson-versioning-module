package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import io.github.liveisgood8.jacksonversioning.holder.VersionHolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class VersioningSerializer extends BeanSerializer implements ResolvableSerializer {

    private final VersionHolder versionHolder;

    public VersioningSerializer(VersionHolder versionHolder, BeanSerializerBase serializer) {
        super(serializer);
        this.versionHolder = versionHolder;
    }

    @Override
    protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeFieldsWithVersioning(bean, gen, provider);
    }

    @Override
    protected void serializeFieldsFiltered(
            Object bean,
            JsonGenerator gen,
            SerializerProvider provider
    ) throws IOException {
        serializeFieldsWithVersioning(bean, gen, provider);
    }

    protected void serializeFieldsWithVersioning(
            Object bean,
            JsonGenerator gen,
            SerializerProvider provider
    ) throws IOException {
        PropertyFilter filter = _propertyFilterId == null
                ? null
                : findPropertyFilter(provider, _propertyFilterId, bean);

        Collection<BeanPropertyWriterWrapper> beanPropertyWriterWrappers = getBeanPropertyWriterWrappers(
                bean,
                provider
        );
        for (var writerWrapper : beanPropertyWriterWrappers) {
            writeProperty(writerWrapper, filter, bean, gen, provider);
        }

        writeAnyGetter(filter, bean, gen, provider);
    }

    private Collection<BeanPropertyWriterWrapper> getBeanPropertyWriterWrappers(
            Object bean,
            SerializerProvider provider
    ) throws IOException {
        BeanPropertyWriter[] beanPropertyWriters = _filteredProps != null && provider.getActiveView() != null
                ? _filteredProps
                : _props;

        Collection<BeanPropertyWriterWrapper> beanPropertyWriterWrappers = new ArrayList<>();
        for (BeanPropertyWriter beanPropertyWriter : beanPropertyWriters) {
            getVersioningPropertyMeta(beanPropertyWriter, bean, provider)
                    .ifPresent(
                            versioningPropertyMeta -> beanPropertyWriterWrappers.add(
                                    new BeanPropertyWriterWrapper(beanPropertyWriter, versioningPropertyMeta)
                            )
                    );
        }

        return beanPropertyWriterWrappers;
    }

    private Optional<VersioningPropertyMeta> getVersioningPropertyMeta(
            BeanPropertyWriter beanPropertyWriter,
            Object bean,
            SerializerProvider provider
    ) throws IOException {
        try {
            return VersioningPropertyMetaGenerator
                    .forProperty(beanPropertyWriter)
                    .getForVersion(versionHolder.getVersion());
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, beanPropertyWriter.getName());
            return Optional.empty();
        }
    }

    private void writeProperty(
            BeanPropertyWriterWrapper writerWrapper,
            PropertyFilter propertyFilter,
            Object bean,
            JsonGenerator gen,
            SerializerProvider provider
    ) throws IOException {
        if (writerWrapper == null) {
            return;
        }

        try {
            BeanPropertyWriterWrapper modifiedProperty = modifyBeanPropertyWriterWrapper(writerWrapper, bean, provider);
            if (propertyFilter == null) {
                modifiedProperty.beanPropertyWriter.serializeAsField(bean, gen, provider);
            } else {
                propertyFilter.serializeAsField(bean, gen, provider, modifiedProperty.beanPropertyWriter);
            }
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, writerWrapper.beanPropertyWriter.getName());
        }
    }

    private void writeAnyGetter(
            PropertyFilter propertyFilter,
            Object bean,
            JsonGenerator gen,
            SerializerProvider provider
    ) throws IOException {
        if (_anyGetterWriter == null) {
            return;
        }

        try {
            _anyGetterWriter.getAndFilter(bean, gen, provider, propertyFilter);
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, "[anySetter]");
        }
    }

    private BeanPropertyWriterWrapper modifyBeanPropertyWriterWrapper(
            BeanPropertyWriterWrapper writerWrapper,
            Object bean,
            SerializerProvider provider
    ) throws IOException {
        VersioningPropertyMeta meta = writerWrapper.versioningPropertyMeta;
        String name = meta.getName();
        if (name != null && !name.isBlank()) {
            var beanPropertyWriter = new CustomBeanPropertyWriter(
                    writerWrapper.beanPropertyWriter,
                    new SerializedString(name)
            );
            return new BeanPropertyWriterWrapper(beanPropertyWriter, writerWrapper.versioningPropertyMeta);
        }

        writerWrapper = applyConverter(writerWrapper, bean, provider);
        writerWrapper = applySerializer(writerWrapper, bean, provider);

        return writerWrapper;
    }

    private BeanPropertyWriterWrapper applyConverter(
            BeanPropertyWriterWrapper writerWrapper,
            Object bean,
            SerializerProvider provider
    ) throws IOException {
        var converterClass = writerWrapper.versioningPropertyMeta.getConverterClass();
        if (converterClass == null) {
            return writerWrapper;
        }

        try {
            var converterConstructor = converterClass.getDeclaredConstructor();
            converterConstructor.setAccessible(true);

            var converter = converterConstructor.newInstance();

            var serializer = new StdDelegatingSerializer(converter);
            return applySerializer(writerWrapper, serializer);
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, writerWrapper.beanPropertyWriter.getName());
            return writerWrapper;
        }
    }

    private BeanPropertyWriterWrapper applySerializer(
            BeanPropertyWriterWrapper writerWrapper,
            Object bean,
            SerializerProvider provider
    ) throws IOException {
        var serializerClass = writerWrapper.versioningPropertyMeta.getSerializerClass();
        if (serializerClass == null) {
            return writerWrapper;
        }

        try {
            var serializerConstructor = serializerClass.getDeclaredConstructor();
            serializerConstructor.setAccessible(true);

            var serializer = serializerConstructor.newInstance();

            return applySerializer(writerWrapper, serializer);
        } catch (Exception e) {
            wrapAndThrow(provider, e, bean, writerWrapper.beanPropertyWriter.getName());
            return writerWrapper;
        }
    }

    private BeanPropertyWriterWrapper applySerializer(
            BeanPropertyWriterWrapper writerWrapper,
            JsonSerializer<?> serializer
    ) {
        BeanPropertyWriter beanPropertyWriter = new CustomBeanPropertyWriter(
                writerWrapper.beanPropertyWriter,
                serializer
        );
        return new BeanPropertyWriterWrapper(beanPropertyWriter, writerWrapper.versioningPropertyMeta);
    }

    private static class BeanPropertyWriterWrapper {
        private final BeanPropertyWriter beanPropertyWriter;
        private final VersioningPropertyMeta versioningPropertyMeta;

        public BeanPropertyWriterWrapper(
                BeanPropertyWriter beanPropertyWriter,
                VersioningPropertyMeta versioningPropertyMeta
        ) {
            this.beanPropertyWriter = beanPropertyWriter;
            this.versioningPropertyMeta = versioningPropertyMeta;
        }
    }

}
