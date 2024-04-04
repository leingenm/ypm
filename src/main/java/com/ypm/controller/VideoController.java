package com.ypm.controller;

import com.ypm.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videosService;

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideos(
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authentication,
        @PathVariable String videoId) throws IOException {

        String accessToken = authentication
            .getAccessToken()
            .getTokenValue();

        videosService.deleteVideo(accessToken, videoId);

        return ResponseEntity.noContent().build();
    }
}
