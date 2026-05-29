package com.example.bugtracker.service;

import com.example.bugtracker.entity.Release;
import com.example.bugtracker.entity.Store;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.entity.enums.ReleaseStatus;
import com.example.bugtracker.repository.ReleaseRepository;
import com.example.bugtracker.repository.StoreRepository;
import com.example.bugtracker.repository.VersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReleaseServiceTest {

    @Mock
    private ReleaseRepository releaseRepository;

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ReleaseService releaseService;

    @Test
    void createRelease_ShouldSetDateAndStatus() {
        Version version = new Version();
        version.setId(1L);
        Store store = new Store(1L, "Play Store");
        when(versionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        Release savedRelease = new Release();
        savedRelease.setId(10L);
        when(releaseRepository.save(any(Release.class))).thenReturn(savedRelease);

        Release result = releaseService.createRelease(1L, 1L, ReleaseStatus.PUBLISHED);
        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(releaseRepository).save(any(Release.class));
    }

    @Test
    void updateRelease_ShouldChangeFields() {
        Release existing = new Release();
        existing.setId(1L);
        existing.setStatus(ReleaseStatus.DRAFT);
        Version oldVersion = new Version();
        oldVersion.setId(1L);
        existing.setVersion(oldVersion);
        Store oldStore = new Store(1L, "old");
        existing.setStore(oldStore);

        Version newVersion = new Version();
        newVersion.setId(2L);
        Store newStore = new Store(2L, "new");

        when(releaseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(versionRepository.findById(2L)).thenReturn(Optional.of(newVersion));
        when(storeRepository.findById(2L)).thenReturn(Optional.of(newStore));
        when(releaseRepository.save(any(Release.class))).thenReturn(existing);

        Release updated = releaseService.updateRelease(1L, 2L, 2L, ReleaseStatus.CANCELLED);
        assertEquals(ReleaseStatus.CANCELLED, updated.getStatus());
        assertEquals(2L, updated.getVersion().getId());
        assertEquals(2L, updated.getStore().getId());
    }
}
