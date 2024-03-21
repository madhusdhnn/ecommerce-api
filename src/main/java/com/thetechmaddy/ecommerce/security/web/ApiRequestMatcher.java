package com.thetechmaddy.ecommerce.security.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public class ApiRequestMatcher implements RequestMatcher {

    private final RequestMatcher delegate = new OrRequestMatcher(getApisToMatch());

    @Override
    public boolean matches(HttpServletRequest request) {
        return delegate.matches(request);
    }

    private static List<RequestMatcher> getApisToMatch() {
        return List.of(
                new AntPathRequestMatcher("/api/products/*"),
                new AntPathRequestMatcher("/api/categories/*"),
                new AntPathRequestMatcher("/api/carts/*"),
                new AntPathRequestMatcher("/api/orders/*"),
                new AntPathRequestMatcher("/api/payments/*")
        );
    }
}
