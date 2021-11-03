package com.nexus.jacksonversioning.annotation;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(JsonVersionedCollection.class)
public @interface JsonVersioned {

    /**
     * Version at which the attribute is added.
     */
    String since() default "";

    /**
     * Version at which the attribute is no longer used.
     */
    String until() default "";

    /**
     * Attribute name in json.
     */
    String name() default "";

    /**
     * Custom serializer for attribute.
     */
    Class<? extends JsonSerializer<?>> serializer() default JsonSerializer.None.class;

    /**
     * Converter for attribute.
     * TODO Implement
     */
    Class<? extends Converter<?, ?>> converter() default Converter.None.class;
}
