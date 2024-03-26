package com.thetechmaddy.ecommerce.models;

import jakarta.servlet.http.HttpServletRequest;

public interface HttpRequestDataParser<T> {

    T parse(HttpServletRequest request);
}
