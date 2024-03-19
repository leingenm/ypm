package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoSnippet;
import com.ypm.model.dto.PlayList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Lazy
public class YouTubeService {

    private final YouTube youTubeClient;

    public Optional<String> getChannelId(String userName) throws IOException {
        var searchRequest = youTubeClient.search().list(List.of("id", "snippet"));
        var searchResult = searchRequest.setQ(userName)
            .setType(List.of("channel"))
            .execute();

        var channelIdOptional = searchResult.getItems().stream()
            .map(item -> item.getSnippet())
            .filter(snippet -> snippet.getChannelTitle().equals(userName) || snippet.getTitle().equals(userName))
            .map(snippet -> snippet.getChannelId())
            .findFirst();

        return channelIdOptional;
    }

    public List<PlayList> getPlaylists(String channelId) throws IOException {
        var request = youTubeClient.playlists().list(List.of("snippet", "contentDetails"));
        var response = request.setMaxResults(25L).setChannelId(channelId).execute();
        var playLists = response.getItems().stream().map(item -> new PlayList(item.getId(), item.getSnippet().getTitle())).toList();

        return playLists;
    }

    public List<VideoSnippet> getPlayListVideos(String playListId) throws IOException {
        var request = youTubeClient.playlistItems().list(List.of("snippet", "contentDetails"));
        var response = request.setMaxResults(25L).setPlaylistId(playListId).execute();
        var videoSnippets = response.getItems().stream().map(item -> new VideoSnippet().setTitle(item.getSnippet().getTitle())).toList();

        return videoSnippets;
    }
}

/*
var playListsRequest = youTubeClient.playlists().list(List.of("snippet", "contentDetails"));
var playListResponse = playListsRequest.setMaxResults(25L).setChannelId(channelId).execute();

var playListIds = playListResponse.getItems().stream().map(Playlist::getId).toList();
var firstPlayListId = playListIds.getFirst();
 */
