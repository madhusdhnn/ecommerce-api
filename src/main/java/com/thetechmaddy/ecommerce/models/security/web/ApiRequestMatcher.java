package com.thetechmaddy.ecommerce.models.security.web;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public final class ApiRequestMatcher extends AbstractRequestMatcher {

    public ApiRequestMatcher() {
        super(getApisToMatch());
    }

    private static List<RequestMatcher> getApisToMatch() {
        return List.of(
                new AntPathRequestMatcher("/api/products/*"),
                new AntPathRequestMatcher("/api/categories/*"),
                new AntPathRequestMatcher("/api/carts/*"),
                new AntPathRequestMatcher("/api/orders/*"),
                new AntPathRequestMatcher("/api/payments/*"),
                new AntPathRequestMatcher("/api/users/addresses")
        );
    }
}
