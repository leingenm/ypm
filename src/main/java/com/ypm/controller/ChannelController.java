package com.ypm.controller;

import com.ypm.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
@Lazy
public class ChannelController {

    private final YouTubeService youTubeService;

    @SneakyThrows // TODO: Delete
    @GetMapping
    public ResponseEntity<String> getChannelId(@RequestParam String channelName) {
        var channelId = youTubeService.getChannelId(channelName).orElseThrow();

        return ResponseEntity.ok(channelId);
    }
}
