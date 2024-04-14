package com.ypm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.ypm.dto.request.MergePlayListsRequest;
import com.ypm.dto.response.ExceptionResponse;
import com.ypm.exception.PlayListNotFoundException;
import com.ypm.service.PlayListService;
import com.ypm.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayListControllerTest {

    @MockBean
    private PlayListService playListService;

    @MockBean
    private VideoService videosService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenCorrectRequest_whenGetPlayLists_thenResponseContainsPlayListName() throws Exception {
        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("Test Playlist"));
        List<Playlist> playlists = Collections.singletonList(playlist);

        when(playListService.getPlayLists(any())).thenReturn(playlists);

        mockMvc.perform(get("/playlists").with(login))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(playlists)));

        verify(playListService, times(1)).getPlayLists(any());
    }

    @Test
    void givenCorrectRequest_whenGetPlayListVideos_thenResponseContainsVideoTitle()
        throws Exception {

        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        PlaylistItem video = new PlaylistItem();
        video.setSnippet(new PlaylistItemSnippet().setTitle("New Title"));
        List<PlaylistItem> videos = List.of(video);

        when(videosService.getPlayListVideos(any(), any())).thenReturn(videos);

        mockMvc.perform(get("/playlists/{playlistId}", "someId").with(login))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(videos)));

        verify(videosService, times(1)).getPlayListVideos(any(), any());
    }

    @Test
    void givenCorrectRequest_whenCreatePlayList_thenResponseContainsCreatedPlayList()
        throws Exception {

        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        Playlist playList = new Playlist();
        playList.setSnippet(new PlaylistSnippet().setTitle("New Playlist Title"));

        when(playListService.createPlayList(any(), eq(playList.getSnippet())))
            .thenReturn(playList);

        mockMvc.perform(post("/playlists")
                .with(login)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playList.getSnippet())))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(playList)));

        verify(playListService, times(1))
            .createPlayList(any(), eq(playList.getSnippet()));
    }

    @Test
    void givenCorrectRequest_whenMergePlayLists_thenResponseContainsMergedPlayList()
        throws Exception {

        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("Merged Playlist Title"));

        MergePlayListsRequest request =
            new MergePlayListsRequest("Merged Playlist Title", List.of("id1", "id2"),
                true);

        when(playListService.mergePlayLists(anyString(), anyString(), anyList(), anyBoolean()))
            .thenReturn(playlist);

        mockMvc.perform(put("/playlists")
                .with(login)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(playlist)));

        verify(playListService, times(1))
            .mergePlayLists(anyString(), anyString(), anyList(), anyBoolean());
    }

    @Test
    void givenCorrectRequest_whenUpdatePlayListTitle_thenResponseContainsUpdatedTitle()
        throws Exception {

        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("New Title"));

        when(playListService.updatePlayListTitle(any(), any(), any())).thenReturn(playlist);

        mockMvc.perform(put("/playlists/{playlistId}", "someId")
                .with(login)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playlist)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(playlist)));

        verify(playListService, times(1))
            .updatePlayListTitle(any(), any(), any());
    }

    @Test
    void givenCorrectRequest_whenMoveVideos_thenResponseContainsMovedVideos() throws Exception {
        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        List<String> videosIds = List.of("videoId1", "videoId2");
        List<PlaylistItem> videos = List.of(new PlaylistItem(), new PlaylistItem());

        when(videosService.moveVideos(any(), any(), any(), any())).thenReturn(videos);

        mockMvc.perform(put("/playlists/{playlistId}/{targetPlaylistId}",
                "someId", "targetId")
                .with(login)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(videosIds)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(videos)));

        verify(videosService, times(1))
            .moveVideos(any(), any(), any(), any());
    }

    @Test
    void givenCorrectRequest_whenDeletePlayList_thenResponseIsOk() throws Exception {
        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        mockMvc.perform(delete("/playlists/{playlistId}", "someId")
                .with(login))
            .andExpect(status().isNoContent());

        verify(playListService, times(1))
            .deletePlayList(any(), any());
    }

    @Test
    void givenPlayListServiceThrowsPlayListNotFoundException_whenUpdatePlayListTitle_thenThrowsException()
        throws Exception {

        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));
        String expectedErrorMessage =
            "Playlist with the 'someId' identifier was not found. Playlist not found";

        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("New Title"));

        when(playListService.updatePlayListTitle(any(), any(), any()))
            .thenThrow(new PlayListNotFoundException("someId", "Playlist not found"));

        mockMvc.perform(put("/playlists/{playlistId}", "someId")
                .with(login)
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

        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        ExceptionResponse response = new ExceptionResponse(500, "IO error occurred",
            Instant.now());

        when(playListService.getPlayLists(any())).thenThrow(new IOException("IO error occurred"));

        mockMvc.perform(get("/playlists").with(login))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value(response.code()))
            .andExpect(jsonPath("$.message").value(response.message()))
            .andExpect(jsonPath("$.date").isString());

        verify(playListService, times(1)).getPlayLists(any());
    }
}
