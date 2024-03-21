package com.thetechmaddy.ecommerce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static <T> T safeParseJSON(ObjectMapper objectMapper, String json, Class<T> target) {
        try {
            return objectMapper.readValue(json, target);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Error while de-serializing as target type : %s", ex.getMessage()), ex);
        }
    }

    public static <T> String safeWriteAsString(ObjectMapper objectMapper, T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Error while serializing as JSON string : %s", ex.getMessage()), ex);
        }
    }
}
