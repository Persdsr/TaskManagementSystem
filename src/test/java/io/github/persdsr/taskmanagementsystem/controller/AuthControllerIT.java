package io.github.persdsr.taskmanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSignUp_InvalidContent_ReturnsBadRequest() throws Exception {
        String invalidRequestJson = """
        {
            "username": "tes",
            "password": "123",
            "email": "invalid"
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Username must be less than 30 characters"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Incorrect email"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("The password must be 8 or more than 8 characters"));
    }
}
