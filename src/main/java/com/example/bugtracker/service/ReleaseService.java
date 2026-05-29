package com.example.bugtracker.service;

import com.example.bugtracker.entity.Release;
import com.example.bugtracker.entity.Store;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.entity.enums.ReleaseStatus;
import com.example.bugtracker.repository.ReleaseRepository;
import com.example.bugtracker.repository.StoreRepository;
import com.example.bugtracker.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReleaseService {
    private final ReleaseRepository releaseRepository;
    private final VersionRepository versionRepository;
    private final StoreRepository storeRepository;

    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
    }

    public Release findById(Long id) {
        return releaseRepository.findById(id).orElseThrow(() -> new RuntimeException("Release not found"));
    }

    @Transactional
    public Release createRelease(Long versionId, Long storeId, ReleaseStatus status) {
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        Release release = new Release();
        release.setVersion(version);
        release.setStore(store);
        release.setReleaseDate(java.time.LocalDate.now());
        release.setStatus(status);
        return releaseRepository.save(release);
    }

    @Transactional
    public Release updateRelease(Long id, Long versionId, Long storeId, ReleaseStatus status) {
        Release release = findById(id);
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version not found"));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        release.setVersion(version);
        release.setStore(store);
        release.setStatus(status);
        // дату релиза не меняем (оставляем оригинальную)
        return releaseRepository.save(release);
    }

    @Transactional
    public void deleteRelease(Long id) {
        releaseRepository.deleteById(id);
    }

    public List<Version> getAllVersions() {
        return versionRepository.findAll();
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
}