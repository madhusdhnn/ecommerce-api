package com.thetechmaddy.ecommerce.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public final class AdminRoleApiRequestMatcher implements RequestMatcher {

    private final String adminApiBasePath;
    private final Set<String> adminRoleApiPaths;

    public AdminRoleApiRequestMatcher(@Value("${server.servlet.context-path}") String contextPath) {
        this.adminApiBasePath = contextPath + "/admin";
        this.adminRoleApiPaths = Set.of("/products");
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return adminRoleApiPaths
                .stream()
                .anyMatch(adminRoleApiPath -> request.getRequestURI().startsWith(adminApiBasePath + adminRoleApiPath));
    }
}
