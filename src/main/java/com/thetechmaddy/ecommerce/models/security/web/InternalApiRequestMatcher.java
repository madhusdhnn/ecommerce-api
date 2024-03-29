package com.thetechmaddy.ecommerce.models.security.web;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public final class InternalApiRequestMatcher extends AbstractRequestMatcher {

    public InternalApiRequestMatcher() {
        super(getApisToMatch());
    }

    private static List<RequestMatcher> getApisToMatch() {
        return List.of(
                new AntPathRequestMatcher("/api/user-provisioning")
        );
    }
}
