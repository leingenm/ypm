package com.ypm.controller;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.VideoSnippet;
import com.ypm.service.PlayListService;
import com.ypm.service.VideosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlayListControllerTest {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayListService playListService;

    @MockBean
    private VideosService videosService;

    @Test
    void givenCorrectRequest_whenGetPlayLists_thenResponseContainsPlayListName() throws Exception {
        Playlist playlist = new Playlist();
        playlist.setSnippet(new PlaylistSnippet().setTitle("Test Playlist"));
        List<Playlist> playlists = Collections.singletonList(playlist);
        when(playListService.getMyPlayLists(any())).thenReturn(playlists);

        mockMvc.perform(get("/playlists")
                .with(oauth2Login()
                    .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].snippet.title").value("Test Playlist"));

        verify(playListService, times(1)).getMyPlayLists(any());
    }

    @Test
    void givenCorrectRequest_whenGetPlayListVideos_thenResponseContainsVideoTitle() throws Exception {
        VideoSnippet video = new VideoSnippet().setTitle("Test Video");
        List<VideoSnippet> videos = Collections.singletonList(video);
        when(videosService.getPlayListVideos(any(), any())).thenReturn(videos);

        mockMvc.perform(get("/playlists/{playlistId}", "playlistId")
                .with(oauth2Login()
                    .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].title").value("Test Video"));

        verify(videosService, times(1)).getPlayListVideos(any(), any());
    }
}
