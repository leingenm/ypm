package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoSnippet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideosService {

    private final YouTube youTubeClient;

    public List<VideoSnippet> getPlayListVideos(String accessToken, String playListId)
        throws IOException {

        var request = youTubeClient.playlistItems().list(List.of("snippet"));
        var response = request
            .setAccessToken(accessToken)
            .setPlaylistId(playListId)
            .execute();

        return response
            .getItems()
            .stream()
            .map(item -> new VideoSnippet().setTitle(item.getSnippet().getTitle()))
            .toList();
    }
}
