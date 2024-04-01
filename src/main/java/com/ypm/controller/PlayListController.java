package com.ypm.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.ypm.service.PlayListService;
import com.ypm.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;
    private final VideoService videosService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getPlayLists(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication) throws IOException {

        String accessToken = authentication
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(playListService.getPlayLists(accessToken));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<List<PlaylistItem>> getPlayListVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String playlistId) throws IOException {

        String accessToken = authentication
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(videosService.getPlayListVideos(accessToken, playlistId));
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlayListTitle(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String playlistId,
        @RequestBody PlaylistSnippet dataWithUpdatedTitle) throws IOException {

        String accessToken = authentication
            .getAccessToken()
            .getTokenValue();

        String newTitle = dataWithUpdatedTitle.getTitle();
        return ResponseEntity.ok(playListService.updatePlayListTitle(accessToken, playlistId, newTitle));
    }

    @PutMapping("/{playlistId}/{targetPlaylistId}")
    public ResponseEntity<List<PlaylistItem>> moveVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String playlistId,
        @PathVariable String targetPlaylistId,
        @RequestBody List<String> videosIds) throws IOException {

        String accessToken = authentication
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(videosService.moveVideos(accessToken, playlistId,
            targetPlaylistId, videosIds));
    }
}
