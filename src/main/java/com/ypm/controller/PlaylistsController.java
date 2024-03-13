package com.ypm.controller;

import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class PlaylistsController {
    private final YouTube youTubeClient;

    // TODO: Move out from here. Added just for demo.
    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.channel.id}")
    private String channelId;

    @Autowired
    public PlaylistsController(@Qualifier("getYouTubeClient") YouTube youTubeClient) {
        this.youTubeClient = youTubeClient;
    }

    @GetMapping("/")
    public ResponseEntity<String> get() throws IOException {
        var request = youTubeClient.channels().list(List.of("contentDetails", "snippet"));
        var response = request.setId(List.of(channelId)).setKey(apiKey).execute();

        return ResponseEntity.ok(response.toString());
    }
}
