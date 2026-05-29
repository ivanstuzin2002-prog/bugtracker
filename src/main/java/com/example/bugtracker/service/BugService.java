package com.example.bugtracker.service;

import com.example.bugtracker.entity.Bug;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.repository.BugRepository;
import com.example.bugtracker.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BugService {
    private final BugRepository bugRepository;
    private final VersionRepository versionRepository;

    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public Bug findById(Long id) {
        return bugRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Bug createBug(Bug bug) {
        return bugRepository.save(bug);
    }

    @Transactional
    public Bug updateBug(Bug bug) {
        return bugRepository.save(bug);
    }

    @Transactional
    public void deleteBug(Long id) {
        bugRepository.deleteById(id);
    }
}