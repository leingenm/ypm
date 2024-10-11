package com.ypm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private String cachedToken;
    private Instant expiresAt;

    public String getToken(OAuth2AuthorizedClient authClient) {
        return authClient.getAccessToken().getTokenValue();
    }

    public String getToken() {
        if (isTokenExpired()) refreshToken();

        return cachedToken;
    }

    private boolean isTokenExpired() {
        return cachedToken == null || Instant.now().isAfter(expiresAt);
    }

    private void refreshToken() {
        var oauthToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        var principalName = oauthToken.getPrincipal().getName();
        var client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);

        if (client == null) {
            var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                                                         .principal(oauthToken.getPrincipal().getName())
                                                         .build();
            client = authorizedClientManager.authorize(authorizeRequest);
        }

        if (client != null) {
            var accessToken = client.getAccessToken();
            cachedToken = accessToken.getTokenValue();
            expiresAt = accessToken.getExpiresAt();
        } else {
            throw new RuntimeException("Problem with OAuth client or token. Maybe unable to refresh token");
        }
    }
}
