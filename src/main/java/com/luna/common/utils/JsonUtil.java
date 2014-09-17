package com.luna.common.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * json转换.
 */
public class JsonUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String toJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T toObj(String jsonString, Class<T> clazz) throws Exception {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        return mapper.readValue(jsonString, clazz);
    }

    /**
     * new TypeReference<List<String>>(){}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T toObj(String jsonString, TypeReference typeReference)
            throws Exception {
    	if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        return (T) mapper.readValue(jsonString, typeReference);
    }

    public String toJsonP(String functionName, Object object)
            throws IOException {
        return toJson(new JSONPObject(functionName, object));
    }
}
