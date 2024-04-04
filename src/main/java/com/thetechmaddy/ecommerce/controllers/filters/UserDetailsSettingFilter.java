package com.thetechmaddy.ecommerce.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetechmaddy.ecommerce.exceptions.AuthenticationMissingException;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.thetechmaddy.ecommerce.models.AppConstants.*;
import static com.thetechmaddy.ecommerce.models.validations.BeanValidator.withValidation;
import static com.thetechmaddy.ecommerce.utils.HttpUtils.sendErrorResponse;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserDetailsSettingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication() instanceof JwtAuthenticationToken token) {
            log.info("Authenticated JWT token found. Building user detail from the token.");
            try {
                CognitoUser user = buildUserDetailFromToken(token);
                request.setAttribute(CURRENT_USER_REQUEST_ATTRIBUTE, user);
                chain.doFilter(request, response);
            } catch (ConstraintViolationException ex) {
                sendErrorResponse(objectMapper, response, INTERNAL_SERVER_ERROR,
                        new RuntimeException("ConstraintViolation: JWT token has malformed data"));
            }
        } else {
            sendErrorResponse(objectMapper, response, UNAUTHORIZED,
                    new AuthenticationMissingException("Authenticated JWT token not found in the request"));
        }
    }

    private static CognitoUser buildUserDetailFromToken(JwtAuthenticationToken auth) {
        return withValidation(() -> {
            Map<String, Object> attributes = auth.getTokenAttributes();

            String cognitoSub = auth.getName();
            String email = (String) attributes.getOrDefault(JWT_TOKEN_EMAIL_ATTRIBUTE_NAME, null);
            String firstName = (String) attributes.getOrDefault(JWT_TOKEN_FIRST_NAME_ATTRIBUTE_NAME, null);
            String lastName = (String) attributes.getOrDefault(JWT_TOKEN_LAST_NAME_ATTRIBUTE_NAME, null);

            return new CognitoUser(cognitoSub, email, firstName, lastName);
        });
    }
}
