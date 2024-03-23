package com.ypm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenOAuth2Login_whenAuthSuccess_thenLoginSuccessful() throws Exception {
        mockMvc.perform(get("/auth/success")
                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("auth"))
            .andExpect(MockMvcResultMatchers.model().attribute("type", "success"))
            .andExpect(MockMvcResultMatchers.model().attribute("title", "Login Successful"))
            .andExpect(MockMvcResultMatchers.model().attribute("message",
                "Welcome back! You have successfully logged in."));
    }

    @Test
    @WithMockUser
    void givenMockUser_whenAuthError_thenLoginError() throws Exception {
        mockMvc.perform(get("/auth/error"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("auth"))
            .andExpect(MockMvcResultMatchers.model().attribute("type", "error"))
            .andExpect(MockMvcResultMatchers.model().attribute("title", "Login Error"))
            .andExpect(MockMvcResultMatchers.model().attribute("message",
                "Sorry, there was an error with your login credentials. Please try again."));
    }

}
