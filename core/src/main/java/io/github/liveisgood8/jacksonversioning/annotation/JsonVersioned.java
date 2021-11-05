package io.github.liveisgood8.jacksonversioning.annotation;

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
     *
     * @return Since version string, i.e. v2.0.0
     */
    String since() default "";

    /**
     * Version at which the attribute is no longer used.
     *
     * @return Until version string, i.e. v3.0.0
     */
    String until() default "";

    /**
     * Attribute name in json.
     *
     * @return Name of attribute.
     */
    String name() default "";

    /**
     * Custom serializer for attribute.
     *
     * @return Serializer class.
     */
    Class<? extends JsonSerializer<?>> serializer() default JsonSerializer.None.class;

    /**
     * Converter for attribute.
     *
     * @return Converter class.
     */
    Class<? extends Converter<?, ?>> converter() default Converter.None.class;
}
