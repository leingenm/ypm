package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class YouTubeServiceTest {
    private final String accessToken =
        "ey.aphktnp.eyJzdWIiOiJ0ZXN0IiwibmFtZSI6IlRlc3QiLCJpYXQiOjE2MjMwNjMwMjJ9";

    @Mock
    private YouTube youTubeClient;

    @InjectMocks
    private PlayListService playListService;

    @InjectMocks
    private VideosService videosService;

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

        List<Playlist> result = playListService.getMyPlayLists(accessToken);

        assertEquals(1, result.size());
        assertEquals(expectedPlaylist, result.get(0));
        assertEquals("Playlist Title", result.get(0).getSnippet().getTitle());
    }

    @Test
    void testGetPlayListVideos() throws IOException {
        String playlistId = "asdsdta_hdfyZSDF";
        var playlistItems = mock(YouTube.PlaylistItems.class);
        var playlistItemsList = mock(YouTube.PlaylistItems.List.class);
        var expectedPlaylistItem = new PlaylistItem();
        var snippet = new PlaylistItemSnippet();
        snippet.setTitle("Video Title");
        expectedPlaylistItem.setSnippet(snippet);
        var playlistItemsListResponse = new PlaylistItemListResponse()
            .setItems(List.of(expectedPlaylistItem));

        when(youTubeClient.playlistItems()).thenReturn(playlistItems);
        when(playlistItems.list(List.of("snippet"))).thenReturn(playlistItemsList);
        when(playlistItemsList.setAccessToken(accessToken)).thenReturn(playlistItemsList);
        when(playlistItemsList.setPlaylistId(playlistId)).thenReturn(playlistItemsList);
        when(playlistItemsList.execute()).thenReturn(playlistItemsListResponse);

        List<VideoSnippet> result = videosService.getPlayListVideos(accessToken, playlistId);

        assertEquals(1, result.size());
        assertEquals("Video Title", result.get(0).getTitle());
    }
}
