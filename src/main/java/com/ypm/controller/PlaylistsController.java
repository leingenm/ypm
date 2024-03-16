package com.ypm.controller;

import com.google.api.services.youtube.model.VideoSnippet;
import com.ypm.model.dto.PlayList;
import com.ypm.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistsController {

    private final YouTubeService youTubeService;

    @SneakyThrows
    @GetMapping("/list")
    public ResponseEntity<List<PlayList>> getPlayLists(@RequestParam String channelId) {
        var results = youTubeService.getPlaylists(channelId);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/list/{playlistId}/videos")
    public ResponseEntity<List<VideoSnippet>> getVideos(@PathVariable String playlistId) throws IOException {
        var playListVideos = youTubeService.getPlayListVideos(playlistId);

        return ResponseEntity.ok(playListVideos);
    }
}
