package com.thetechmaddy.ecommerce.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

import static com.thetechmaddy.ecommerce.utils.NumberUtils.formatAsTwoDecimalPlaces;

public class BigDecimalTwoDecimalPlacesSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        double val = formatAsTwoDecimalPlaces(value).doubleValue();
        gen.writeNumber(val);
    }
}
