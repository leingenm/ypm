package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.ypm.constant.Part;
import com.ypm.constant.PrivacyStatus;
import com.ypm.dto.PlaylistDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayListServiceTest {

    private final String accessToken =
        "ey.aphktnp.eyJzdWIiOiJ0ZXN0IiwibmFtZSI6IlRlc3QiLCJpYXQiOjE2MjMwNjMwMjJ9";

    @Mock
    private YouTube youTubeClient;

    @Mock
    private VideoServiceImp videoService;

    @InjectMocks
    private PlayListServiceImp playListService;

    @Test
    void givenCorrectData_whenGetPlayLists_thenPlaylistsFound() throws IOException {
        var playlists = mock(YouTube.Playlists.class);
        var playlistsList = mock(YouTube.Playlists.List.class);
        var expectedPlaylist = new Playlist();
        expectedPlaylist.setSnippet(new PlaylistSnippet().setTitle("Playlist Title"));
        var playlistsListResponse = new PlaylistListResponse()
            .setItems(List.of(expectedPlaylist));

        when(youTubeClient.playlists()).thenReturn(playlists);
        when(playlists.list(List.of(Part.SNIPPET.toString()))).thenReturn(playlistsList);
        when(playlistsList.setAccessToken(accessToken)).thenReturn(playlistsList);
        when(playlistsList.setMine(true)).thenReturn(playlistsList);
        when(playlistsList.setMaxResults(50L)).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);

        List<Playlist> result = playListService.getPlayLists(accessToken);

        assertEquals(1, result.size());
        assertEquals(expectedPlaylist, result.get(0));
        assertEquals("Playlist Title", result.get(0).getSnippet().getTitle());
    }

    @Test
    void givenCorrectData_whenMergePlayLists_thenPlayListsMerged() throws IOException {
        var playlists = mock(YouTube.Playlists.class);
        var playlistsList = mock(YouTube.Playlists.List.class);
        var playlistsInsert = mock(YouTube.Playlists.Insert.class);
        var playlistItems = mock(YouTube.PlaylistItems.class);
        var playlistItemsList = mock(YouTube.PlaylistItems.List.class);
        var playlistItemsInsert = mock(YouTube.PlaylistItems.Insert.class);
        var playlistItemsDelete = mock(YouTube.PlaylistItems.Delete.class);
        var playlistDelete = mock(YouTube.Playlists.Delete.class);
        var expectedPlaylist = new Playlist();
        expectedPlaylist.setSnippet(new PlaylistSnippet().setTitle("Merged Playlist"));
        var playlistsListResponse = new PlaylistListResponse()
            .setItems(List.of(expectedPlaylist));
        var playListItemsListResponse =
            new PlaylistItemListResponse().setItems(List.of(new PlaylistItem()));

        when(youTubeClient.playlists()).thenReturn(playlists);
        when(youTubeClient.playlistItems()).thenReturn(playlistItems);
        when(playlists.list(List.of(Part.SNIPPET.toString()))).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);
        when(playlists.insert(any(), any())).thenReturn(playlistsInsert);
        when(playlistsInsert.setAccessToken(anyString())).thenReturn(playlistsInsert);
        when(playlistsInsert.execute()).thenReturn(expectedPlaylist);
        when(playlistItems.list(List.of(Part.SNIPPET.toString()))).thenReturn(playlistItemsList);
        when(playlistItemsList.setAccessToken(accessToken)).thenReturn(playlistItemsList);
        when(playlistItemsList.execute()).thenReturn(playListItemsListResponse);
        when(playlistItems.insert(eq(List.of(Part.SNIPPET.toString())), any()))
            .thenReturn(playlistItemsInsert);
        when(playlistItemsDelete.setAccessToken(accessToken)).thenReturn(playlistItemsDelete);
        when(playlists.delete(anyString())).thenReturn(playlistDelete);
        when(playlistDelete.setAccessToken(accessToken)).thenReturn(playlistDelete);

        assertNotNull(playlistsInsert, "playlistsInsert mock is null");

        var playlistDto = new PlaylistDto("Merged Playlist", Optional.empty(), PrivacyStatus.PRIVATE.toString());
        Playlist result = playListService.mergePlayLists(accessToken, playlistDto, List.of("playlistId1", "playlistId2"), true);

        assertEquals(expectedPlaylist, result);
        assertEquals("Merged Playlist", result.getSnippet().getTitle());
    }

    @Test
    void givenCorrectData_whenCreatePlayList_thenPlayListCreated() throws IOException {
        var playlists = mock(YouTube.Playlists.class);
        var playlistsInsert = mock(YouTube.Playlists.Insert.class);
        var expectedPlaylist = new Playlist()
            .setSnippet(new PlaylistSnippet().setTitle("Playlist Title"))
            .setStatus(new PlaylistStatus().setPrivacyStatus(PrivacyStatus.PRIVATE.toString()));

        when(youTubeClient.playlists()).thenReturn(playlists);
        when(playlists.insert(List.of(Part.STATUS.toString(), Part.SNIPPET.toString()), expectedPlaylist)).thenReturn(playlistsInsert);
        when(playlistsInsert.setAccessToken(accessToken)).thenReturn(playlistsInsert);
        when(playlistsInsert.execute()).thenReturn(expectedPlaylist);

        var playlistDto = new PlaylistDto("Playlist Title", Optional.empty(), PrivacyStatus.PRIVATE.toString());
        Playlist result = playListService.createPlayList(accessToken, playlistDto);

        assertEquals(expectedPlaylist, result);
        assertEquals("Playlist Title", result.getSnippet().getTitle());
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
        when(playlists.list(List.of(Part.SNIPPET.toString()))).thenReturn(playlistsList);
        when(playlistsList.setAccessToken(accessToken)).thenReturn(playlistsList);
        when(playlistsList.setMine(true)).thenReturn(playlistsList);
        when(playlistsList.setId(any())).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);
        when(playlists.update(List.of(Part.SNIPPET.toString()), expectedPlaylist)).thenReturn(playlistUpdate);
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
        when(playlists.list(List.of(Part.SNIPPET.toString()))).thenReturn(playlistsList);
        when(playlistsList.setAccessToken(accessToken)).thenReturn(playlistsList);
        when(playlistsList.setMine(true)).thenReturn(playlistsList);
        when(playlistsList.setId(List.of("playlistId"))).thenReturn(playlistsList);
        when(playlistsList.execute()).thenReturn(playlistsListResponse);

        Playlist result = playListService.getPlayListById(accessToken, "playlistId");

        assertEquals(expectedPlaylist, result);
        assertEquals("Playlist Title", result.getSnippet().getTitle());
    }

    @Test
    void givenExistingPlayList_whenDeletePlayList_thenNoException() throws IOException {
        var playLists = mock(YouTube.Playlists.class);
        var playListsDelete = mock(YouTube.Playlists.Delete.class);
        String playListId = "asdsdta_hdfyZSDF";

        when(youTubeClient.playlists()).thenReturn(playLists);
        when(playLists.delete(playListId)).thenReturn(playListsDelete);
        when(playListsDelete.setAccessToken(accessToken)).thenReturn(playListsDelete);

        playListService.deletePlayList(accessToken, playListId);

        verify(playLists, times(1)).delete(playListId);
    }
}
