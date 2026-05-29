package com.example.bugtracker.service;

import com.example.bugtracker.entity.Application;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.repository.ApplicationRepository;
import com.example.bugtracker.repository.VersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private VersionService versionService;

    @Test
    void createVersion_ShouldSetAppAndCreatedAt() {
        Application app = new Application(1L, "TestApp");
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(app));
        Version versionToSave = new Version();
        versionToSave.setVersion("1.0");
        versionToSave.setBuildNumber(100);
        when(versionRepository.save(any(Version.class))).thenAnswer(i -> i.getArgument(0));

        Version created = versionService.createVersion(versionToSave, 1L);
        assertNotNull(created);
        assertEquals("TestApp", created.getApplication().getName());
        assertNotNull(created.getCreatedAt());
        verify(versionRepository).save(any(Version.class));
    }
}