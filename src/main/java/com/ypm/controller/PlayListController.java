package com.ypm.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.VideoSnippet;
import com.ypm.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlayListController {
    private final YouTubeService youTubeService;

    @GetMapping("/list")
    public ResponseEntity<List<Playlist>> getPlayLists(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication) throws IOException {

        String accessToken = authentication.getAccessToken().getTokenValue();
        return ResponseEntity.ok(youTubeService.getMyPlayLists(accessToken));
    }

    @GetMapping("/{playlistId}/videos")
    public ResponseEntity<List<VideoSnippet>> getVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String playlistId) throws IOException {

        String accessToken = authentication.getAccessToken().getTokenValue();
        return ResponseEntity.ok(youTubeService.getPlayListVideos(accessToken, playlistId));
    }
}
