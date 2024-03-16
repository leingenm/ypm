package com.ypm.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class YouTubeClientConfiguration {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.application.name}")
    private String applicationName;

    @Bean
    public YouTube getYouTubeClient() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new YouTube.Builder(httpTransport, GsonFactory.getDefaultInstance(), null)
            .setApplicationName(applicationName)
            .setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey))
            .build();
    }
}
