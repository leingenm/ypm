package com.ypm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.*;
import com.ypm.constant.PrivacyStatus;
import com.ypm.dto.PlaylistDto;
import com.ypm.dto.request.MergePlayListsRequest;
import com.ypm.dto.response.ExceptionResponse;
import com.ypm.exception.PlayListNotFoundException;
import com.ypm.service.youtube.PlayListServiceImp;
import com.ypm.service.youtube.VideoServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayListControllerTest {

    @MockBean
    private PlayListServiceImp playListService;

    @MockBean
    private VideoServiceImp videosService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenCorrectRequest_whenGetPlayLists_thenResponseContainsPlayListName() throws Exception {
        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("Test Playlist"));
        List<Playlist> playlists = Collections.singletonList(playlist);

        when(playListService.getPlayLists(any())).thenReturn(playlists);

        mockMvc.perform(get("/playlists").header(AUTHORIZATION, "Bearer token"))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(playlists)));

        verify(playListService, times(1)).getPlayLists(any());
    }

    @Test
    void givenCorrectRequest_whenGetPlayListVideos_thenResponseContainsVideoTitle()
            throws Exception {
        PlaylistItem video = new PlaylistItem();
        video.setSnippet(new PlaylistItemSnippet().setTitle("New Title"));
        List<PlaylistItem> videos = List.of(video);

        when(videosService.getPlayListVideos(any(), any())).thenReturn(videos);

        mockMvc.perform(get("/playlists/{playlistId}", "someId").header(AUTHORIZATION, "Bearer token"))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(videos)));

        verify(videosService, times(1)).getPlayListVideos(any(), any());
    }

    @Test
    void givenCorrectRequest_whenCreatePlayList_thenResponseContainsCreatedPlayList()
            throws Exception {

        var playlistTitle = "New Playlist Title";
        var playList = new Playlist()
                .setSnippet(new PlaylistSnippet().setTitle(playlistTitle))
                .setStatus(new PlaylistStatus().setPrivacyStatus(PrivacyStatus.PRIVATE.toString()));
        var playlistDto = new PlaylistDto(playlistTitle, Optional.empty(), PrivacyStatus.PRIVATE.toString());

        when(playListService.createPlayList(any(), eq(playlistDto)))
                .thenReturn(playList);

        mockMvc.perform(post("/playlists")
                                .header(AUTHORIZATION, "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(playlistDto)))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(playList)));

        verify(playListService, times(1))
                .createPlayList(any(), eq(playlistDto));
    }

    @Test
    void givenCorrectRequest_whenCreateUnlistedPlaylist_thenResponseContainsCreatedPlaylist() throws Exception {
        var playlistTitle = "New Unlisted Playlist";
        var playlistDescription = "Description";
        var playlist = new Playlist()
                .setSnippet(new PlaylistSnippet().setTitle(playlistTitle).setDescription(playlistDescription))
                .setStatus(new PlaylistStatus().setPrivacyStatus(PrivacyStatus.UNLISTED.toString()));

        var playlistDto = new PlaylistDto(playlistTitle, Optional.of(playlistDescription),
                                          PrivacyStatus.UNLISTED.toString());

        when(playListService.createPlayList(any(), eq(playlistDto)))
                .thenReturn(playlist);

        mockMvc.perform(post("/playlists")
                                .header(AUTHORIZATION, "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(playlistDto)))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(playlist)));
    }

    @Test
    void givenCorrectRequest_whenMergePlayLists_thenResponseContainsMergedPlayList()
            throws Exception {

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("Merged Playlist Title"));

        var playlistDto = new PlaylistDto("Merged Playlist Title", Optional.of("Description"),
                                          PrivacyStatus.PRIVATE.toString());
        MergePlayListsRequest request =
                new MergePlayListsRequest(playlistDto, List.of("id1", "id2"), true);

        when(playListService.mergePlayLists(anyString(), any(PlaylistDto.class), anyList(), anyBoolean()))
                .thenReturn(playlist);

        mockMvc.perform(put("/playlists")
                                .header(AUTHORIZATION, "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(playlist)));

        verify(playListService, times(1))
                .mergePlayLists(anyString(), any(PlaylistDto.class), anyList(), anyBoolean());
    }

    @Test
    void givenCorrectRequest_whenUpdatePlayListTitle_thenResponseContainsUpdatedTitle()
            throws Exception {

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("New Title"));

        when(playListService.updatePlayListTitle(any(), any(), any())).thenReturn(playlist);

        mockMvc.perform(put("/playlists/{playlistId}", "someId")
                                .header(AUTHORIZATION, "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(playlist)))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(playlist)));

        verify(playListService, times(1))
                .updatePlayListTitle(any(), any(), any());
    }

    @Test
    void givenCorrectRequest_whenMoveVideos_thenResponseContainsMovedVideos() throws Exception {
        List<String> videosIds = List.of("videoId1", "videoId2");
        List<PlaylistItem> videos = List.of(new PlaylistItem(), new PlaylistItem());

        when(videosService.moveVideos(any(), any(), any(), any())).thenReturn(videos);

        mockMvc.perform(put("/playlists/{playlistId}/{targetPlaylistId}",
                            "someId", "targetId")
                                .header(AUTHORIZATION, "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(videosIds)))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(videos)));

        verify(videosService, times(1))
                .moveVideos(any(), any(), any(), any());
    }

    @Test
    void givenCorrectRequest_whenDeletePlayList_thenResponseIsOk() throws Exception {
        mockMvc.perform(delete("/playlists/{playlistId}", "someId").header(AUTHORIZATION, "Bearer token"))
               .andExpect(status().isNoContent());

        verify(playListService, times(1))
                .deletePlayList(any(), any());
    }

    @Test
    void givenPlayListServiceThrowsPlayListNotFoundException_whenUpdatePlayListTitle_thenThrowsException()
            throws Exception {
        String expectedErrorMessage =
                "Playlist with the 'someId' identifier was not found. Playlist not found";

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("New Title"));

        when(playListService.updatePlayListTitle(any(), any(), any()))
                .thenThrow(new PlayListNotFoundException("someId", "Playlist not found"));

        mockMvc.perform(put("/playlists/{playlistId}", "someId")
                                .header(AUTHORIZATION, "Bearer token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(playlist)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.code").value(404))
               .andExpect(jsonPath("$.message").value(expectedErrorMessage))
               .andExpect(jsonPath("$.date").isString())
               .andExpect(result ->
                                  assertInstanceOf(PlayListNotFoundException.class, result.getResolvedException()))
               .andExpect(result -> assertEquals(expectedErrorMessage,
                                                 Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(playListService, times(1))
                .updatePlayListTitle(any(), any(), any());
    }

    @Test
    void givenPlayListServiceThrowsIOException_whenGetPlayLists_thenThrowsIOException()
            throws Exception {

        ExceptionResponse response = new ExceptionResponse(500, "IO error occurred",
                                                           Instant.now());

        when(playListService.getPlayLists(any())).thenThrow(new IOException("IO error occurred"));

        mockMvc.perform(get("/playlists").header(AUTHORIZATION, "Bearer token"))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.code").value(response.code()))
               .andExpect(jsonPath("$.message").value(response.message()))
               .andExpect(jsonPath("$.date").isString());

        verify(playListService, times(1)).getPlayLists(any());
    }
}
