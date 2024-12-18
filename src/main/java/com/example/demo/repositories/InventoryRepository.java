package com.example.demo.repositories;

import com.example.demo.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    boolean existsByUsername(String username);
    List<InventoryEntity> findAllByUsername(String username);
    @Modifying
    @Query("DELETE FROM InventoryEntity i WHERE i.username = :username")
    void deleteAllByUsername(@Param("username") String username);

}
