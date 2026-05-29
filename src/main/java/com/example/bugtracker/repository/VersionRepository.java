package com.example.bugtracker.repository;

import com.example.bugtracker.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Long> {
}