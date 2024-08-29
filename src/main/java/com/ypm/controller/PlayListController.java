package com.ypm.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.ypm.dto.PlaylistDto;
import com.ypm.dto.request.MergePlayListsRequest;
import com.ypm.service.PlayListService;
import com.ypm.service.TokenService;
import com.ypm.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getPlayLists(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        return ResponseEntity.ok(playListService.getPlayLists(accessToken));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<List<PlaylistItem>> getPlayListVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        var playlistVideos = videosService.getPlayListVideos(accessToken, playlistId);
        return ResponseEntity.ok(playlistVideos);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @RequestBody PlaylistDto playlistDto
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        var createdPlayList = playListService.createPlayList(accessToken, playlistDto);
        return ResponseEntity.ok(createdPlayList);
    }

    @PutMapping()
    public ResponseEntity<Playlist> mergePlayLists(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @RequestBody MergePlayListsRequest request
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        var mergedPlaylist = playListService.mergePlayLists(accessToken, request.playlistDto(),
            request.playListsIds(), request.deleteAfterMerge());
        return ResponseEntity.ok(mergedPlaylist);
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlayListTitle(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId,
        @RequestBody PlaylistSnippet dataWithUpdatedTitle
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        var newTitle = dataWithUpdatedTitle.getTitle();
        var updatedPlaylist = playListService.updatePlayListTitle(accessToken, playlistId, newTitle);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @PutMapping("/{playlistId}/{targetPlaylistId}")
    public ResponseEntity<List<PlaylistItem>> moveVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId,
        @PathVariable String targetPlaylistId,
        @RequestBody List<String> videosIds
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        var movedVideos = videosService.moveVideos(accessToken, playlistId, targetPlaylistId, videosIds);
        return ResponseEntity.ok(movedVideos);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlayList(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        playListService.deletePlayList(accessToken, playlistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{playlistId}/export")
    public ResponseEntity<Resource> exportPlaylistAsCsv(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String playlistId
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        var filePath = playListService.exportPlaylist(accessToken, playlistId);
        var file = new FileSystemResource(filePath);

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .contentType(MediaType.parseMediaType("application/csv"))
            .body(file);
    }
}
