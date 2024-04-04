package com.thetechmaddy.ecommerce.models.security;

import com.thetechmaddy.ecommerce.exceptions.ApiKeyRequiredException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Objects;
import java.util.function.Supplier;

@Log4j2
public class InternalApiKeyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final String internalApiKey;
    private final InternalApiKeyParser apiKeyParser;

    public InternalApiKeyAuthorizationManager(String internalApiKey) {
        this.internalApiKey = internalApiKey;
        this.apiKeyParser = new InternalApiKeyParser();
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        boolean authorized = false;
        try {
            String apiKey = apiKeyParser.parse(object.getRequest());
            authorized = Objects.equals(internalApiKey, apiKey);
        } catch (ApiKeyRequiredException ex) {
            log.error(ex.getMessage());
        }
        return new AuthorizationDecision(authorized);
    }
}
