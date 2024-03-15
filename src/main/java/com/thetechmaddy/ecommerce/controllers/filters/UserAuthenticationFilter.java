package com.thetechmaddy.ecommerce.controllers.filters;

import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.thetechmaddy.ecommerce.models.AppConstants.*;

@Log4j2
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication() instanceof JwtAuthenticationToken token) {
            log.info("Authenticated JWT token found. Building user detail from the token.");
            CognitoUser user = buildUserDetailFromToken(token);
            request.setAttribute(CURRENT_USER_REQUEST_ATTRIBUTE, user);
        }

        chain.doFilter(request, response);
    }

    private static CognitoUser buildUserDetailFromToken(JwtAuthenticationToken auth) {
        Map<String, Object> attributes = auth.getTokenAttributes();

        String cognitoSub = auth.getName();
        String email = (String) attributes.getOrDefault(JWT_TOKEN_EMAIL_ATTRIBUTE_NAME, null);
        String firstName = (String) attributes.getOrDefault(JWT_TOKEN_FIRST_NAME_ATTRIBUTE_NAME, null);
        String lastName = (String) attributes.getOrDefault(JWT_TOKEN_LAST_NAME_ATTRIBUTE_NAME, null);

        return new CognitoUser(cognitoSub, email, firstName, lastName);
    }
}
