package com.thetechmaddy.ecommerce.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.thetechmaddy.ecommerce.utils.HttpUtils.sendErrorResponse;

@Log4j2
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    public static final String CURRENT_USER_COGNITO_SUB_ATTRIBUTE = "com.thetechmaddy.ecommerce.controllers.filters." + "currentUser_cognitoSub";

    private final ObjectMapper objectMapper;

    @Autowired
    public UserAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();

        try {
            if (context.getAuthentication() instanceof JwtAuthenticationToken auth) {
                String cognitoSub = auth.getName();
                request.setAttribute(CURRENT_USER_COGNITO_SUB_ATTRIBUTE, cognitoSub);
            }
        } catch (UsernameNotFoundException ex) {
            log.error("Encountered error while finding user with current authentication token", ex);
            sendErrorResponse(objectMapper, response, HttpStatus.FORBIDDEN, () -> ex);
            return;
        }

        chain.doFilter(request, response);
    }
}
