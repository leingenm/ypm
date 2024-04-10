package com.ypm.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.ypm.dto.request.MergePlayListsRequest;
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
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(playListService.getPlayLists(accessToken));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<List<PlaylistItem>> getPlayListVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(videosService.getPlayListVideos(accessToken, playlistId));
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlayList(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @RequestBody PlaylistSnippet playlistSnippet) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(playListService.createPlayList(accessToken, playlistSnippet));
    }

    @PutMapping()
    public ResponseEntity<Playlist> mergePlayLists(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @RequestBody MergePlayListsRequest request) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(playListService.mergePlayLists(accessToken,
            request.mergedPlayListTitle(), request.playListsIds(), request.deleteAfterMerge()));
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlayListTitle(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId,
        @RequestBody PlaylistSnippet dataWithUpdatedTitle) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        String newTitle = dataWithUpdatedTitle.getTitle();
        return ResponseEntity.ok(playListService.updatePlayListTitle(accessToken, playlistId,
            newTitle));
    }

    @PutMapping("/{playlistId}/{targetPlaylistId}")
    public ResponseEntity<List<PlaylistItem>> moveVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId,
        @PathVariable String targetPlaylistId,
        @RequestBody List<String> videosIds) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        return ResponseEntity.ok(videosService.moveVideos(accessToken, playlistId,
            targetPlaylistId, videosIds));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlayList(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId) throws IOException {

        String accessToken = authClient
            .getAccessToken()
            .getTokenValue();

        playListService.deletePlayList(accessToken, playlistId);
        return ResponseEntity.noContent().build();
    }
}
