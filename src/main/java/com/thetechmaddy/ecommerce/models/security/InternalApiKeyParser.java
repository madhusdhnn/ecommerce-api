package com.thetechmaddy.ecommerce.models.security;

import com.thetechmaddy.ecommerce.exceptions.ApiKeyRequiredException;
import com.thetechmaddy.ecommerce.models.HttpRequestDataParser;
import jakarta.servlet.http.HttpServletRequest;

import static com.thetechmaddy.ecommerce.models.AppConstants.INTERNAL_API_KEY_HEADER_NAME;

public final class InternalApiKeyParser implements HttpRequestDataParser<String> {

    @Override
    public String parse(HttpServletRequest request) {
        String apiKey = request.getHeader(INTERNAL_API_KEY_HEADER_NAME);
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiKeyRequiredException(String.format("%s header is missing", INTERNAL_API_KEY_HEADER_NAME));
        }
        return apiKey;
    }
}
