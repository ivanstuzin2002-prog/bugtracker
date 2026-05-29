package com.example.bugtracker.controller;

import com.example.bugtracker.entity.enums.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerIntegrationTest extends BaseControllerTest {

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListUsers_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUser_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/users/create")
                        .with(csrf())
                        .param("lastName", "Test")
                        .param("firstName", "User")
                        .param("email", "testuser@example.com")
                        .param("password", "pass")
                        .param("roleName", RoleName.TESTER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}