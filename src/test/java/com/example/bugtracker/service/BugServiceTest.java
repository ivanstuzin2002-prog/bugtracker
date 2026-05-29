package com.example.bugtracker.service;

import com.example.bugtracker.entity.Bug;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.entity.enums.Priority;
import com.example.bugtracker.repository.BugRepository;
import com.example.bugtracker.repository.VersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BugServiceTest {

    @Mock
    private BugRepository bugRepository;

    @Mock
    private VersionRepository versionRepository;

    @InjectMocks
    private BugService bugService;

    private Bug testBug;
    private Version testVersion;

    @BeforeEach
    void setUp() {
        testVersion = new Version();
        testVersion.setId(1L);
        testVersion.setVersion("1.0.0");
        testBug = new Bug();
        testBug.setId(1L);
        testBug.setTitle("Test bug");
        testBug.setPriority(Priority.HIGH);
        testBug.setVersion(testVersion);
    }

    @Test
    void getAllBugs_ShouldReturnList() {
        when(bugRepository.findAll()).thenReturn(List.of(testBug));
        List<Bug> bugs = bugService.getAllBugs();
        assertThat(bugs).hasSize(1);
        assertThat(bugs.get(0).getTitle()).isEqualTo("Test bug");
    }

    @Test
    void findById_ShouldReturnBug() {
        when(bugRepository.findById(1L)).thenReturn(Optional.of(testBug));
        Bug found = bugService.findById(1L);
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void createBug_ShouldSave() {
        when(bugRepository.save(any(Bug.class))).thenReturn(testBug);
        Bug saved = bugService.createBug(testBug);
        assertThat(saved).isEqualTo(testBug);
        verify(bugRepository, times(1)).save(testBug);
    }
}