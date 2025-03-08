package io.github.persdsr.taskmanagementsystem.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addTask_UserNotAuthorized_ReturnsBadRequest() throws Exception {
        String requestJson = """
            {
                "title": "Task 1 title",
                "description": "Task 1 description",
                "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addTask_UserNotAdmin_ReturnsForbiddenRequest() throws Exception {
        String requestJson = """
            {
                "title": "Task 1 title",
                "description": "Task 1 description",
                "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/api/task")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }
}
