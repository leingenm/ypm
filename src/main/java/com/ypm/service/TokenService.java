package com.ypm.service;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String getToken(OAuth2AuthorizedClient authClient) {
        return authClient.getAccessToken().getTokenValue();
    }
}
