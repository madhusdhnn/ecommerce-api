package com.thetechmaddy.ecommerce.security.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public final class AdminRoleApiRequestMatcher implements RequestMatcher {

    private final String apiBasePath;
    private final Set<String> adminRoleApiPaths;

    public AdminRoleApiRequestMatcher(@Value("${server.servlet.context-path}") String contextPath) {
        this.apiBasePath = contextPath + "/api";
        this.adminRoleApiPaths = Set.of("/products");
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return adminRoleApiPaths
                .stream()
                .anyMatch(adminRoleApiPath -> request.getRequestURI().startsWith(apiBasePath + adminRoleApiPath));
    }
}
