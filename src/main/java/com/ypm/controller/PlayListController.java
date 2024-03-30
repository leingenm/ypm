package com.ypm.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.VideoSnippet;
import com.ypm.service.PlayListService;
import com.ypm.service.VideosService;
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
    private final VideosService videosService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getPlayLists(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication) throws IOException {

        String accessToken = authentication.getAccessToken().getTokenValue();
        return ResponseEntity.ok(playListService.getMyPlayLists(accessToken));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<List<VideoSnippet>> getPlayListVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String playlistId) throws IOException {

        String accessToken = authentication.getAccessToken().getTokenValue();
        return ResponseEntity.ok(videosService.getPlayListVideos(accessToken, playlistId));
    }

    @PutMapping("/{playlistId}/updateTitle")
    public ResponseEntity<Playlist> updatePlayListName(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String playlistId,
        @RequestBody PlaylistSnippet dataWithUpdatedTitle) throws IOException {

        String accessToken = authentication.getAccessToken().getTokenValue();
        String newTitle = dataWithUpdatedTitle.getTitle();
        return ResponseEntity.ok(playListService.updatePlayListName(accessToken, playlistId, newTitle));
    }
}
