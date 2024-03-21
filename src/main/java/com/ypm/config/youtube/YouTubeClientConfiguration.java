package com.ypm.config.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
@Lazy
public class YouTubeClientConfiguration {

    @Bean
    public YouTube getYouTubeClient() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        return new YouTube.Builder(httpTransport, jsonFactory, null)
            .setApplicationName("YouTube Playlists Helper")
            .build();
    }
}
