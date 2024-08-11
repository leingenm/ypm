package com.ypm.controller;

import com.ypm.dto.VideoDto;
import com.ypm.service.TokenService;
import com.ypm.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videosService;
    private final TokenService tokenService;

    @PostMapping("/loadData")
    public ResponseEntity<List<VideoDto>> getVideoData(@RequestBody List<String> videoIds) throws IOException {
        var videoData = videosService.getVideoData(videoIds);

        return ResponseEntity.ok(videoData);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authClient,
        @PathVariable String videoId
    ) throws IOException {
        var accessToken = tokenService.getToken(authClient);
        videosService.deleteVideo(accessToken, videoId);
        return ResponseEntity.noContent().build();
    }
}
