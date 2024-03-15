package com.thetechmaddy.ecommerce.security.web;

import com.thetechmaddy.ecommerce.models.PathAndHttpMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

public abstract class PathAndHttpMethodMatcher implements RequestMatcher {

    public abstract String getApiBasePath();

    public boolean matches(HttpServletRequest request, PathAndHttpMethod pm) {
        String requestURI = request.getRequestURI();
        return pm.method().matches(request.getMethod())
                && requestURI.startsWith(getApiBasePath() + pm.path());
    }
}
