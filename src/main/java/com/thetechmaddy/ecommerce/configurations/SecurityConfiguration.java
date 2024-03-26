package com.thetechmaddy.ecommerce.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetechmaddy.ecommerce.controllers.filters.UserAuthenticationFilter;
import com.thetechmaddy.ecommerce.security.CognitoGroupToGrantedAuthorityConverter;
import com.thetechmaddy.ecommerce.security.web.AdminRoleApiRequestMatcher;
import com.thetechmaddy.ecommerce.security.web.ApiRequestMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import static com.thetechmaddy.ecommerce.models.AppConstants.COGNITO_ADMIN_GROUP_NAME;
import static com.thetechmaddy.ecommerce.models.AppConstants.COGNITO_USER_GROUP_NAME;
import static com.thetechmaddy.ecommerce.utils.HttpUtils.sendErrorResponse;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfiguration {

    @Value("${cognito.jwks-uri}")
    private String jwksUri;

    private final ObjectMapper objectMapper;

    private final UserAuthenticationFilter userAuthenticationFilter;
    private final CognitoGroupToGrantedAuthorityConverter cognitoGroupToGrantedAuthorityConverter;

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(userAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistryCustomizer())
                .exceptionHandling(exceptionHandlingConfigurerCustomizer())
                .oauth2ResourceServer(oAuth2ResourceServerConfigurerCustomizer())
                .build();
    }

    private Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> oAuth2ResourceServerConfigurerCustomizer() {
        return (resourceServerConfigurer) -> resourceServerConfigurer.jwt(
                jwtConfigurer -> {
                    jwtConfigurer.jwkSetUri(jwksUri);
                    jwtConfigurer.jwtAuthenticationConverter(cognitoGroupToGrantedAuthorityConverter);
                }
        );
    }

    private Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingConfigurerCustomizer() {
        return exCustomizer -> exCustomizer.authenticationEntryPoint(
                (request, response, authException) ->
                        sendErrorResponse(objectMapper, response, UNAUTHORIZED, () -> authException));
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizationManagerRequestMatcherRegistryCustomizer() {
        // @formatter:off
        return (httpRequestsAuthorizer) -> httpRequestsAuthorizer
                    .requestMatchers(HttpMethod.GET, "/ping")
                        .permitAll()
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**"
                    ).permitAll()
                    .requestMatchers(new AdminRoleApiRequestMatcher())
                        .hasRole(COGNITO_ADMIN_GROUP_NAME)
                    .requestMatchers(new ApiRequestMatcher())
                        .hasAnyRole(COGNITO_ADMIN_GROUP_NAME, COGNITO_USER_GROUP_NAME)
                    .anyRequest()
                        .authenticated();
        // @formatter:on
    }
}