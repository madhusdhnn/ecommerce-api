package com.thetechmaddy.ecommerce.models.responses;

import com.thetechmaddy.ecommerce.models.SerializableEnum;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public enum ApiResponseStatus implements SerializableEnum {
    SUCCESS("success"), FAILED("failed");

    private final String status;

    public static ApiResponseStatus of(String value) {
        Objects.requireNonNull(value);
        return switch (value.toLowerCase()) {
            case "success" -> ApiResponseStatus.SUCCESS;
            case "failed" -> ApiResponseStatus.FAILED;
            default -> throw new IllegalArgumentException(String.format("Invalid response status value : %s", value));
        };
    }

    @Override
    public String value() {
        return this.status;
    }
}
