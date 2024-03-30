package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayListService {

    private final YouTube youTubeClient;

    public List<Playlist> getMyPlayLists(String accessToken) throws IOException {
        var request = youTubeClient.playlists().list(List.of("snippet"));
        var response = request
            .setAccessToken(accessToken)
            .setMine(true)
            .execute();

        return response.getItems();
    }

    public Playlist updatePlayListName(String accessToken, String playListId, String newTitle)
        throws IOException {

        Playlist playlistToEdit = getPlayListById(accessToken, playListId);
        playlistToEdit.getSnippet().setTitle(newTitle);
        var request = youTubeClient.playlists().update(List.of("snippet"), playlistToEdit);
        return request.setAccessToken(accessToken).execute();
    }


    public Playlist getPlayListById(String accessToken, String playListId) throws IOException {
        var request = youTubeClient.playlists().list(List.of("snippet"));
        var response = request
            .setAccessToken(accessToken)
            .setMine(true)
            .execute();

        return response
            .getItems()
            .stream()
            .filter(playlist -> playlist.getId().equals(playListId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Playlist not found."));
    }
}
