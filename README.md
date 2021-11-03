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
    
    @JsonSince("v4.2.0")
    private String name;

    @JsonUntil("v3.0.0")
    private Integer amount;

    // constructors, getters, setters...
}
```

Also, you have an ability to change properties name for specific versions.

```java
public class ModelData {

    @JsonUntil(version = "v3.0.0", name = "amount")
    private Integer amountLegacy;

    @JsonSince("v3.0.0")
    private Double amount;  
    
    // constructors, getters, setters...
}
```