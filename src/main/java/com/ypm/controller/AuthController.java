package com.ypm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping
    public void login() {
        System.out.println("Hello from AuthController");
    }

    @GetMapping("/google")
    public ResponseEntity<?> googleAuth(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        var token = authorizedClient.getAccessToken();
        var refresh = authorizedClient.getRefreshToken();

        return ResponseEntity.ok(token);
    }
}
