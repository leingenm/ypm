package com.ypm.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

@Configuration
public class YouTubeClientConfiguration {
    private static final Collection<String> SCOPES =
            List.of("https://www.googleapis.com/auth/youtube.readonly");

    @Value("${youtube.application.name}")
    private String applicationName;

    @Bean
    public YouTube getYouTubeClient() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        // Load client secrets.
        InputStream in = YouTubeClientConfiguration.class.getResourceAsStream("/client_secret.json");
        if (in == null) throw new IOException("client_secret.json not found");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
                .build();
        Credential credential =
            new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver.Builder().setPort(8888).build()).authorize("user");


        return new YouTube.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(applicationName)
            .build();
    }
}
