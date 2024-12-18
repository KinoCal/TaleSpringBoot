package com.example.demo.repositories;


import com.example.demo.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.PlayerEntity;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByUsername(String username);
}

