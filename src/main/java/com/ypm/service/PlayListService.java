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

    public List<Playlist> getPlayLists(String accessToken) throws IOException {
        return youTubeClient
            .playlists()
            .list(List.of("snippet"))
            .setAccessToken(accessToken)
            .setMine(true)
            .execute()
            .getItems();
    }

    public Playlist updatePlayListTitle(String accessToken, String playListId,
                                        String newTitle) throws IOException {

        Playlist playlistToEdit = getPlayListById(accessToken, playListId);
        playlistToEdit.getSnippet().setTitle(newTitle);

        return youTubeClient
            .playlists()
            .update(List.of("snippet"), playlistToEdit)
            .setAccessToken(accessToken)
            .execute();
    }

    public Playlist getPlayListById(String accessToken, String playListId) throws IOException {
        return youTubeClient
            .playlists()
            .list(List.of("snippet"))
            .setId(List.of(playListId))
            .setAccessToken(accessToken)
            .setMine(true)
            .execute()
            .getItems()
            .stream()
            .findFirst()
            .orElseThrow();
    }
}
