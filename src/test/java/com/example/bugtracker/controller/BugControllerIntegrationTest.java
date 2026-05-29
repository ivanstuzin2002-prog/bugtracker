package com.example.bugtracker.controller;

import com.example.bugtracker.entity.Bug;
import com.example.bugtracker.entity.User;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.entity.enums.Priority;
import com.example.bugtracker.repository.BugRepository;
import com.example.bugtracker.repository.UserRepository;
import com.example.bugtracker.repository.VersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BugControllerIntegrationTest extends BaseControllerTest {

    @Autowired
    private BugRepository bugRepository;
    @Autowired
    private VersionRepository versionRepository;
    @Autowired
    private UserRepository userRepository;

    private Version testVersion;
    private User testUser;

    @BeforeEach
    void setUp() {
        bugRepository.deleteAll();
        // создать тестовую версию
        testVersion = versionRepository.findAll().stream().findFirst()
                .orElseGet(() -> {
                    Version v = new Version();
                    v.setVersion("1.0-test");
                    v.setBuildNumber(1);
                    return versionRepository.save(v);
                });
        // создать тестового пользователя, если нет
        testUser = userRepository.findByEmail("admin@example.com").orElse(null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListBugs_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bugs"))
                .andExpect(status().isOk())
                .andExpect(view().name("bugs"));
    }

    @Test
    @WithMockUser(roles = "DEVELOPER")
    void testCreateBugForm_ShouldReturnForm() throws Exception {
        mockMvc.perform(get("/bugs/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("bug-form"));
    }

    @Test
    @WithMockUser(roles = "DEVELOPER")
    void testCreateBug_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/bugs/create")
                        .with(csrf())
                        .param("title", "Integration Bug")
                        .param("description", "Desc")
                        .param("priority", "HIGH")
                        .param("versionId", testVersion.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bugs"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteBug_ShouldRedirect() throws Exception {
        Bug bug = new Bug();
        bug.setTitle("ToDelete");
        bug.setPriority(Priority.MEDIUM);
        bug.setVersion(testVersion);
        bug.setReportedBy(testUser);
        Bug saved = bugRepository.save(bug);

        mockMvc.perform(get("/bugs/delete/{id}", saved.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bugs"));
    }
}