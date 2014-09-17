package com.luna.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * json转换.
 */
public class JsonMapper {
    private ObjectMapper mapper;

    /** constructor. */
    public JsonMapper() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public String toJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) throws IOException {
        if ((jsonString == null) || "".equals(jsonString.trim())) {
            return null;
        }

        return mapper.readValue(jsonString, clazz);
    }

    /**
     * new TypeReference<List<String>>(){}
     */
    public <T> T fromJson(String jsonString, TypeReference typeReference)
            throws IOException {
        if ((jsonString == null) || "".equals(jsonString.trim())) {
            return null;
        }

        return (T) mapper.readValue(jsonString, typeReference);
    }

    public String toJsonP(String functionName, Object object)
            throws IOException {
        return toJson(new JSONPObject(functionName, object));
    }
}
