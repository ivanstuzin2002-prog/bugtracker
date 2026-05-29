package com.example.bugtracker.service;

import com.example.bugtracker.entity.Application;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.repository.ApplicationRepository;
import com.example.bugtracker.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;
    private final ApplicationRepository applicationRepository;

    public List<Version> getAllVersions() {
        return versionRepository.findAll();
    }

    public Version findById(Long id) {
        return versionRepository.findById(id).orElseThrow();
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Transactional
    public Version createVersion(Version version, Long appId) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        version.setApplication(app);
        version.setCreatedAt(LocalDate.now());
        return versionRepository.save(version);
    }
}