package com.thetechmaddy.ecommerce.security.web;

import com.thetechmaddy.ecommerce.models.PathAndHttpMethod;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public final class AdminRoleApiRequestMatcher extends PathAndHttpMethodMatcher {

    @Getter
    private final String apiBasePath;
    private final Set<PathAndHttpMethod> adminRoleApiPaths;

    public AdminRoleApiRequestMatcher(@Value("${server.servlet.context-path}") String contextPath) {
        this.apiBasePath = contextPath + "/api";
        this.adminRoleApiPaths = Set.of(PathAndHttpMethod.of(HttpMethod.POST, "/products"));
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return adminRoleApiPaths.stream().anyMatch(pm -> super.matches(request, pm));
    }
}
