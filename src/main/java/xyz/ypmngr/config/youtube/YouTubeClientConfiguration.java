package xyz.ypmngr.config.youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YouTubeClientConfiguration {

    @Bean
    public YouTube youTubeClient() throws GeneralSecurityException, IOException {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        var jsonFactory = GsonFactory.getDefaultInstance();
        return new YouTube.Builder(httpTransport, jsonFactory, null)
            .setApplicationName("YouTube Playlists Helper")
            .build();
    }
}
