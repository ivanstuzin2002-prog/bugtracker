package com.example.bugtracker.repository;

import com.example.bugtracker.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}