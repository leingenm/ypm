package com.ypm.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;

class YouTubeServiceTest {

    @InjectMocks
    private YouTubeService youTubeService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getChannelIdTest() throws IOException {
        var channelId = youTubeService.getChannelId("Nick Chapsas").get();
        System.out.println(channelId);
    }
}
