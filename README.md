Introduction
================================================
Jackson module which provide versioning feature to object mapper.
It gives you an ability to specify since and until version for POJOs classes.

Using
================================================

## Register module

At the beginning you should register versioning module in your Jackson object mapper.

```java
public class ObjectMapperProvider {

    public static ObjectMapper createObjectMapper(Version version) {
        var objectMapper = new ObjectMapper();
        
        // At this line module with specified version is registered
        objectMapper.registerModule(new JsonVersioningModule("v1.5.0"));

        return objectMapper;
    }
}
```

## Versioning

You can use versioning feature for serialize POJO classes.

```java
public class ModelData {
    
    @JsonVersioned(since = "v4.2.0")
    private String name;

    @JsonVersioned(until = "v3.0.0")
    private Integer amount;

    // constructors, getters, setters...
}
```

Also, you have an ability to change properties name for specific versions.

```java
public class ModelData {

    @JsonVersioned(since = "v3.0.0", name = "amount")
    private Integer amountLegacy;

    @JsonVersioned(since = "v3.0.0")
    private Double amount;  
    
    // constructors, getters, setters...
}
```

You can define custom serializer or converter for properties.

```java
public class ConverterExampleModel {

    @JsonVersioned(
            since = "v2.0",
            until = "v3.0",
            converter = FirstConverter.class
    )
    @JsonVersioned(
            since = "v3.0",
            until = "v4.0",
            converter = SecondConverter.class
    )
    private String amount;

    // constructors, getters, setters...

    private static class FirstConverter extends StdConverter<String, Integer> {

        @Override
        public Integer convert(String value) {
            return 500;
        }
    }

    private static class SecondConverter extends StdConverter<String, Double> {

        @Override
        public Double convert(String value) {
            return 1000.0;
        }
    }
}

public class SerializerExampleModel {

    @JsonVersioned(
            since = "v2.0",
            until = "v3.0",
            serializer = FirstSerializer.class
    )
    @JsonVersioned(
            since = "v3.0",
            until = "v4.0",
            serializer = SecondSerializer.class
    )
    private String name;

    // constructors, getters, setters...

    private static class FirstSerializer extends JsonSerializer<String> {

        @Override
        public void serialize(
                String value,
                JsonGenerator gen,
                SerializerProvider provider
        ) throws IOException {
            gen.writeString("name1");
        }
    }

    private static class SecondSerializer extends JsonSerializer<String> {

        @Override
        public void serialize(
                String value,
                JsonGenerator gen,
                SerializerProvider provider
        ) throws IOException {
            gen.writeString("name2");
        }
    }
}
```