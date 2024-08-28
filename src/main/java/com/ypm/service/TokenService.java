package com.ypm.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public TokenService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public String getToken(OAuth2AuthorizedClient authClient) {
        return authClient.getAccessToken().getTokenValue();
    }

    public String getToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var oauthToken = (OAuth2AuthenticationToken) authentication;
        var clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        var principalName = oauthToken.getPrincipal().getName();
        var client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);

        return client.getAccessToken().getTokenValue();
    }
}
