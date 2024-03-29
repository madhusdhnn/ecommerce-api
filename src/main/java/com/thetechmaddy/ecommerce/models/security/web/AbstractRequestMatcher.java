package com.thetechmaddy.ecommerce.models.security.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public abstract class AbstractRequestMatcher implements RequestMatcher {

    private final RequestMatcher delegateMatcher;

    protected AbstractRequestMatcher(List<RequestMatcher> requestMatchers) {
        this.delegateMatcher = new OrRequestMatcher(requestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return delegateMatcher.matches(request);
    }
}
