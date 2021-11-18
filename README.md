Introduction
================================================
![Maven central badge](https://img.shields.io/maven-central/v/io.github.liveisgood8/jackson-versioning-module)

Jackson module which provide versioning feature to object mapper. It gives you an ability to specify since and until
version for POJOs classes.

Getting started
================================================

## Adding library

To add a dependency on Jackson versioning module using Maven, use the following:

```xml

<dependency>
    <groupId>io.github.liveisgood8</groupId>
    <artifactId>jackson-versioning-module</artifactId>
    <version>2.0.0</version>
</dependency>
```

To add a dependency using Gradle:

```groovy
implementation 'io.github.liveisgood8:jackson-versioning-module:2.0.0'
```

## Register module

At the beginning you should register versioning module in your Jackson object mapper.

```java
public class ObjectMapperProvider {

    public static ObjectMapper createObjectMapper(Version version) {
        ObjectMapper objectMapper = new ObjectMapper();

        // At this line module with specified version is registered
        objectMapper.registerModule(
                new JsonVersioningModule(new SimpleVersionHolder("v1.5.0"))
        );

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

You can see for full example in `example` folder.

## Version holder

Version holder provide information about current version for module.

### SimpleVersionHolder

Hold constant version.

```java
public class Example {
    
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(
                new JsonVersioningModule(new SimpleVersionHolder("v1.5.0"))
        );
        return objectMapper;
    }
}
```

### ThreadLocalVersionHolder

Hold constant version locally for thread.

```java
public class Example {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(
                new JsonVersioningModule(ThreadLocalHolder.get())
        );
        return objectMapper;
    }
}
```

Somewhere in application code (for example in spring request filter):
```java
public class Filter {

    public void doFilter() {
        Version version = getCurrentVersion();
        ThreadLocalVersionHolder.initialize(version);
        
        // ...
    }
    
    private Version getCurrentVersion() {
        // ...
    }
}
```

### InheritableThreadLocalVersionHolder

Same as `ThreadLocalVersionHolder`, but version information is diving into child threads.

```java
public class Example {

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(
                new JsonVersioningModule(InheritableThreadLocalHolder.get())
        );
        return objectMapper;
    }
}
```