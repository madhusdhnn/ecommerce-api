package com.thetechmaddy.ecommerce.models.security.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public final class AdminRoleApiRequestMatcher implements RequestMatcher {

    private final RequestMatcher delegate = new OrRequestMatcher(getAdminApisToMatch());

    @Override
    public boolean matches(HttpServletRequest request) {
        return delegate.matches(request);
    }

    private static List<RequestMatcher> getAdminApisToMatch() {
        return List.of(new AntPathRequestMatcher("/api/products", HttpMethod.POST.name()));
    }
}
