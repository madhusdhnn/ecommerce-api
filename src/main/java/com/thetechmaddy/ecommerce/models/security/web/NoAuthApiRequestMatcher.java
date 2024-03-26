package com.thetechmaddy.ecommerce.models.security.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class NoAuthApiRequestMatcher implements RequestMatcher {

    private final RequestMatcher delegate = new OrRequestMatcher(getApisToMatch());

    @Override
    public boolean matches(HttpServletRequest request) {
        return delegate.matches(request);
    }

    private static List<RequestMatcher> getApisToMatch() {
        return List.of(
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/ping", GET.name())
        );
    }
}
