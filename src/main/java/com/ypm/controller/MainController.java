package com.ypm.controller;

import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
public class MainController {
    private final YouTube youTube;

    @Autowired
    public MainController(@Qualifier("getYouTube") YouTube youTube) {
        this.youTube = youTube;
    }

    @GetMapping("/")
    public String get() throws IOException {
        YouTube.Channels.List request = youTube.channels().list(List.of(""));

        return String.valueOf(request.execute());
    }
}
