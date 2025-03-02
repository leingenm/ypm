package com.ypm.controller;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import com.ypm.service.youtube.VideoServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VideoControllerTest {

    @MockBean
    private VideoServiceImp videosService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenCorrectRequest_whenDeleteVideos_thenNoContent() throws Exception {
        doNothing().when(videosService).deleteVideo(any(), any());

        mockMvc.perform(delete("/videos/{videoId}", "someId").header(AUTHORIZATION, "Bearer token"))
               .andExpect(status().isNoContent());
    }

    @Test
    void givenVideoServiceThrowsGoogleJsonResponseException_whenDeleteVideos_thenThrowsException()
            throws Exception {
        GoogleJsonError error = new GoogleJsonError();
        error.setCode(400);
        error.setMessage("Something went wrong!");
        GoogleJsonResponseException googleJsonResponseException = new GoogleJsonResponseException(
                new HttpResponseException.Builder(400, "Something went wrong!",
                                                  new HttpHeaders()), error);

        doThrow(googleJsonResponseException).when(videosService).deleteVideo(any(), any());

        mockMvc.perform(delete("/videos/{videoId}", "someId").header(AUTHORIZATION, "Bearer token"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400))
               .andExpect(jsonPath("$.message").value("Something went wrong!"))
               .andExpect(jsonPath("$.date").isString());

        verify(videosService, times(1)).deleteVideo(any(), any());
    }

}
