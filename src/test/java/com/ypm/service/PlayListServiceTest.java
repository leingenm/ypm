package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.PlaylistSnippet;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayListServiceTest {

    private final String accessToken =
        "ey.aphktnp.eyJzdWIiOiJ0ZXN0IiwibmFtZSI6IlRlc3QiLCJpYXQiOjE2MjMwNjMwMjJ9";

    @Mock
    private YouTube youTubeClient;

    @InjectMocks
    private PlayListService playListService;

    @Test
    void givenCorrectData_whenGetPlayLists_thenPlaylistsFound() throws IOException {
        var playlists = mock(YouTube.Playlists.class);
        var playlistsList = mock(YouTube.Playlists.List.class);
        var expectedPlaylist = new Playlist();
        expectedPlaylist.setSnippet(new PlaylistSnippet().setTitle("Playlist Title"));
        var playlistsListResponse = new PlaylistListResponse()
            .setItems(List.of(expectedPlaylist));

        when(youTubeClient.playlists()).thenReturn(playlists);
        when(playlists.list(List.of("snippet"))).thenReturn(playlistsList);
        when(playlistsList.setAccessToken(accessToken)).thenReturn(playlistsList);
        when(playlistsList.setMine(true)).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);

        List<Playlist> result = playListService.getPlayLists(accessToken);

        assertEquals(1, result.size());
        assertEquals(expectedPlaylist, result.get(0));
        assertEquals("Playlist Title", result.get(0).getSnippet().getTitle());
    }

    @Test
    void givenCorrectData_whenUpdatePlayListTitle_thenPlayListTitleUpdated() throws IOException {
        var playlists = mock(YouTube.Playlists.class);
        var playlistsList = mock(YouTube.Playlists.List.class);
        var playlistUpdate = mock(YouTube.Playlists.Update.class);
        var expectedPlaylist = new Playlist();
        expectedPlaylist.setSnippet(new PlaylistSnippet().setTitle("Playlist Title"));
        var playlistsListResponse = new PlaylistListResponse()
            .setItems(List.of(expectedPlaylist));

        when(youTubeClient.playlists()).thenReturn(playlists);
        when(playlists.list(List.of("snippet"))).thenReturn(playlistsList);
        when(playlistsList.setAccessToken(accessToken)).thenReturn(playlistsList);
        when(playlistsList.setMine(true)).thenReturn(playlistsList);
        when(playlistsList.setId(any())).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);
        when(playlists.update(List.of("snippet"), expectedPlaylist)).thenReturn(playlistUpdate);
        when(playlistUpdate.setAccessToken(accessToken)).thenReturn(playlistUpdate);
        when(playlistUpdate.execute()).thenReturn(expectedPlaylist);

        Playlist result = playListService.updatePlayListTitle(accessToken, "playlistId", "New Title");

        assertEquals("New Title", result.getSnippet().getTitle());
    }

    @Test
    void givenCorrectData_whenGetPlayListById_thenPlayListFound() throws IOException {
        var playlists = mock(YouTube.Playlists.class);
        var playlistsList = mock(YouTube.Playlists.List.class);
        var expectedPlaylist = new Playlist();
        expectedPlaylist.setSnippet(new PlaylistSnippet().setTitle("Playlist Title"));
        var playlistsListResponse = new PlaylistListResponse()
            .setItems(List.of(expectedPlaylist));

        when(youTubeClient.playlists()).thenReturn(playlists);
        when(playlists.list(List.of("snippet"))).thenReturn(playlistsList);
        when(playlistsList.setAccessToken(accessToken)).thenReturn(playlistsList);
        when(playlistsList.setMine(true)).thenReturn(playlistsList);
        when(playlistsList.setId(List.of("playlistId"))).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);

        Playlist result = playListService.getPlayListById(accessToken, "playlistId");

        assertEquals(expectedPlaylist, result);
        assertEquals("Playlist Title", result.getSnippet().getTitle());
    }
}
