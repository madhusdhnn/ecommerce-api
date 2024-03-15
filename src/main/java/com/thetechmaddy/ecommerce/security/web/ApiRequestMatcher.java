package com.thetechmaddy.ecommerce.security.web;

import com.thetechmaddy.ecommerce.models.PathAndHttpMethod;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class ApiRequestMatcher extends PathAndHttpMethodMatcher {

    @Getter
    private final String apiBasePath;
    private final Set<PathAndHttpMethod> apiPaths;

    public ApiRequestMatcher(@Value("${server.servlet.context-path}") String contextPath) {
        this.apiBasePath = contextPath + "/api";
        this.apiPaths = Collections.emptySet();
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (apiPaths.isEmpty()) {
            return request.getRequestURI().startsWith(apiBasePath);
        }
        return apiPaths.stream().anyMatch(pm -> super.matches(request, pm));
    }
}
