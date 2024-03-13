package com.thetechmaddy.ecommerce.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiRequestMatcher implements RequestMatcher {

    private final String apiBasePath;

    public ApiRequestMatcher(@Value("${server.servlet.context-path}") String contextPath) {
        this.apiBasePath = contextPath + "/api";
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return request.getRequestURI().startsWith(apiBasePath);
    }
}
