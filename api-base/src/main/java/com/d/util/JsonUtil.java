package com.d.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("all")
public class JsonUtil {
    private ObjectMapper objectMapper = new ObjectMapper();
    private static JsonUtil instance;

    public static Map fromJson(String json) {
        return single().fromJson(json, Map.class);
    }

    public <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> String toJson(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String toJsonString(T t) {
        return single().toJson(t);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> fromJsonToList(String json, Class<T> valueType) {
        return fromJsonWrapper(json, ArrayList.class, valueType);
    }

    public <E> E fromJsonWrapper(String json, Class<E> e, Class<?>... valueType) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(e, valueType);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    public static JsonUtil build() {
        return new JsonUtil();
    }

    public JsonUtil nullIgnore() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return this;
    }

    public JsonUtil nullContain() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return this;
    }

    public JsonUtil snakeCase() {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return this;
    }

    public static JsonUtil single() {
        return instance;
    }

    static {
        instance = new JsonUtil();
        instance.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
