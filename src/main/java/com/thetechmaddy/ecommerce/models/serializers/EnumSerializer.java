package com.thetechmaddy.ecommerce.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.thetechmaddy.ecommerce.models.SerializableEnum;

import java.io.IOException;

public class EnumSerializer<T extends SerializableEnum> extends JsonSerializer<T> {

    @Override
    public void serialize(T _enum, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(_enum.value());
    }
}
