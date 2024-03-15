package com.thetechmaddy.ecommerce.models;


import org.springframework.http.HttpMethod;

public record PathAndHttpMethod(HttpMethod method, String path) {

    public static PathAndHttpMethod of(HttpMethod method, String path) {
        return new PathAndHttpMethod(method, path);
    }
}
