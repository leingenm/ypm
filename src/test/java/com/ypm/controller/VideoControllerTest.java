package com.ypm.controller;

import com.ypm.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VideoControllerTest {

    @MockBean
    private VideoService videosService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenCorrectRequest_whenDeleteVideos_thenNoContent() throws Exception {
        var login = oauth2Login()
            .clientRegistration(this.clientRegistrationRepository.findByRegistrationId("google"));

        doNothing().when(videosService).deleteVideo(any(), any());

        mockMvc.perform(delete("/videos/{videoId}", "someId").with(login))
            .andExpect(status().isNoContent());
    }
}
