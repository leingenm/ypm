package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VideoServiceTest {

    private final String accessToken =
        "ey.aphktnp.eyJzdWIiOiJ0ZXN0IiwibmFtZSI6IlRlc3QiLCJpYXQiOjE2MjMwNjMwMjJ9";

    @Mock
    private YouTube youTubeClient;

    @InjectMocks
    private VideoService videoService;

    @Test
    void givenExistingPlayList_whenGetPlayListVideos_thenPlayListVideosReturned() throws IOException {
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

        List<PlaylistItem> result = videoService.getPlayListVideos(accessToken, playlistId);

        assertEquals(1, result.size());
        assertEquals("Video Title", result.get(0).getSnippet().getTitle());
    }

    @Test
    void givenExistingVideos_whenGetVideos_thenVideosReturned() throws IOException {
        List<String> videosIds = List.of("videoId1", "videoId2");
        var playlistItems = mock(YouTube.PlaylistItems.class);
        var playlistItemsList = mock(YouTube.PlaylistItems.List.class);
        var expectedPlaylistItem = new PlaylistItem();
        var snippet = new PlaylistItemSnippet();
        snippet.setTitle("Video Title");
        expectedPlaylistItem.setSnippet(snippet);
        var playlistItemsListResponse = new PlaylistItemListResponse()
            .setItems(List.of(expectedPlaylistItem, expectedPlaylistItem));

        when(youTubeClient.playlistItems()).thenReturn(playlistItems);
        when(playlistItems.list(List.of("snippet"))).thenReturn(playlistItemsList);
        when(playlistItemsList.setAccessToken(accessToken)).thenReturn(playlistItemsList);
        when(playlistItemsList.setId(videosIds)).thenReturn(playlistItemsList);
        when(playlistItemsList.execute()).thenReturn(playlistItemsListResponse);

        List<PlaylistItem> result = videoService.getVideos(accessToken, videosIds);

        assertEquals(2, result.size());
        assertEquals("Video Title", result.get(0).getSnippet().getTitle());
    }

    @Test
    void givenCorrectVideoData_whenInsertVideo_thenInsertedVideoReturned() throws IOException {
        var playlistItems = mock(YouTube.PlaylistItems.class);
        var playlistItemsInsert = mock(YouTube.PlaylistItems.Insert.class);
        var expectedPlaylistItem = new PlaylistItem();
        var snippet = new PlaylistItemSnippet();
        snippet.setTitle("Video Title");
        expectedPlaylistItem.setSnippet(snippet);

        when(youTubeClient.playlistItems()).thenReturn(playlistItems);
        when(playlistItems.insert(List.of("snippet"), expectedPlaylistItem)).thenReturn(playlistItemsInsert);
        when(playlistItemsInsert.setAccessToken(accessToken)).thenReturn(playlistItemsInsert);
        when(playlistItemsInsert.execute()).thenReturn(expectedPlaylistItem);

        PlaylistItem result = videoService.insertVideo(accessToken, expectedPlaylistItem);

        assertEquals("Video Title", result.getSnippet().getTitle());
    }

    @Test
    void givenExistingVideo_whenDeleteVideo_thenNoException() throws IOException {
        var playlistItems = mock(YouTube.PlaylistItems.class);
        var playlistItemsDelete = mock(YouTube.PlaylistItems.Delete.class);
        String videoId = "asdsdta_hdfyZSDF";

        when(youTubeClient.playlistItems()).thenReturn(playlistItems);
        when(playlistItems.delete(videoId)).thenReturn(playlistItemsDelete);
        when(playlistItemsDelete.setAccessToken(accessToken)).thenReturn(playlistItemsDelete);

        videoService.deleteVideo(accessToken, videoId);

        verify(playlistItems, times(1)).delete(videoId);
    }

    @Test
    void givenExistingVideos_whenMoveVideos_thenMovedVideosReturned() throws IOException {
        String playListId = "asdsdta_hdfyZSDF";
        String targetPlayListId = "asdsdta_hdfyZSDF";
        List<String> videosIds = List.of("videoId1", "videoId2");
        var playlistItems = mock(YouTube.PlaylistItems.class);
        var playlistItemsList = mock(YouTube.PlaylistItems.List.class);
        var playlistItemsInsert = mock(YouTube.PlaylistItems.Insert.class);
        var playlistItemsDelete = mock(YouTube.PlaylistItems.Delete.class);
        var expectedPlaylistItem = new PlaylistItem();
        var snippet = new PlaylistItemSnippet();
        snippet.setPlaylistId("asdsdta_hdfyZSDF");
        snippet.setTitle("Video Title");
        expectedPlaylistItem.setSnippet(snippet);
        var playlistItemsListResponse = new PlaylistItemListResponse()
            .setItems(List.of(expectedPlaylistItem, expectedPlaylistItem));

        when(youTubeClient.playlistItems()).thenReturn(playlistItems);
        when(playlistItems.list(List.of("snippet"))).thenReturn(playlistItemsList);
        when(playlistItems.insert(List.of("snippet"), expectedPlaylistItem)).thenReturn(playlistItemsInsert);
        when(playlistItems.delete(any())).thenReturn(playlistItemsDelete);
        when(playlistItemsList.setAccessToken(accessToken)).thenReturn(playlistItemsList);
        when(playlistItemsList.setId(videosIds)).thenReturn(playlistItemsList);
        when(playlistItemsList.execute()).thenReturn(playlistItemsListResponse);
        when(playlistItemsInsert.execute()).thenReturn(expectedPlaylistItem);
        when(playlistItemsInsert.setAccessToken(accessToken)).thenReturn(playlistItemsInsert);
        when(playlistItemsDelete.setAccessToken(accessToken)).thenReturn(playlistItemsDelete);

        List<PlaylistItem> result = videoService.moveVideos(accessToken, playListId, targetPlayListId, videosIds);

        assertEquals(2, result.size());
        assertEquals("Video Title", result.get(0).getSnippet().getTitle());
    }
}
