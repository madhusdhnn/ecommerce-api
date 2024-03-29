package com.thetechmaddy.ecommerce.models.security.web;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public final class AdminRoleApiRequestMatcher extends AbstractRequestMatcher {

    public AdminRoleApiRequestMatcher() {
        super(getAdminApisToMatch());
    }

    private static List<RequestMatcher> getAdminApisToMatch() {
        return List.of(new AntPathRequestMatcher("/api/products", HttpMethod.POST.name()));
    }
}
