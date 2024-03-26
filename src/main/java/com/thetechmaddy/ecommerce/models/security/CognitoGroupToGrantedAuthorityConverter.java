package com.thetechmaddy.ecommerce.models.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.thetechmaddy.ecommerce.models.AppConstants.COGNITO_GROUPS_CLAIM_NAME;

@Component
public final class CognitoGroupToGrantedAuthorityConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    @SuppressWarnings("unchecked")
    public JwtAuthenticationToken convert(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        if (!claims.containsKey(COGNITO_GROUPS_CLAIM_NAME)) {
            return new JwtAuthenticationToken(jwt);
        }

        List<String> groups = (List<String>) claims.get(COGNITO_GROUPS_CLAIM_NAME);
        List<SimpleGrantedAuthority> grantedAuthorities = groups.stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .toList();

        return new JwtAuthenticationToken(jwt, grantedAuthorities);
    }
}
