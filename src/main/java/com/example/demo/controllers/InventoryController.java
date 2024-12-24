package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.InventoryEntity;
import com.example.demo.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.PlayerEntity;
import com.example.demo.repositories.PlayerRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerInventory(@RequestBody InventoryEntity inventory) {
        // Check if inventory already exists for the username
        if (inventoryRepository.existsByUsername(inventory.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Inventory already exists");
        }

        // Create 30 default inventory slots
        List<InventoryEntity> defaultInventories = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            InventoryEntity newInventory = new InventoryEntity();
            newInventory.setUsername(inventory.getUsername());
            newInventory.setName("Dagger");
            newInventory.setType("weapon");
            newInventory.setQuantity(1);
            defaultInventories.add(newInventory);
        }

        // Save all default inventory slots
        inventoryRepository.saveAll(defaultInventories);

        return ResponseEntity.ok("Inventory created successfully");
    }



    @GetMapping("/load/{username}")
    public ResponseEntity<?> loadInventory(@PathVariable String username) {
        // Find all inventory items for the username
        List<InventoryEntity> inventories = inventoryRepository.findAllByUsername(username);

        if (inventories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No inventory found for this user");
        }

        return ResponseEntity.ok(inventories);
    }


    @PutMapping("/update")
    @Transactional
    public ResponseEntity<?> updateInventory(@RequestBody List<InventoryEntity> updatedInventoryList) {
        if (updatedInventoryList == null || updatedInventoryList.isEmpty()) {
            return ResponseEntity.badRequest().body("Inventory list cannot be null or empty");
        }

        String username = updatedInventoryList.get(0).getUsername();
        List<InventoryEntity> existingInventory = inventoryRepository.findAllByUsername(username);

        // Update existing rows
        for (int i = 0; i < existingInventory.size(); i++) {
            System.out.println(updatedInventoryList.get(i).getName());

            existingInventory.get(i).setName(updatedInventoryList.get(i).getName());
            existingInventory.get(i).setType(updatedInventoryList.get(i).getType());
            existingInventory.get(i).setQuantity(updatedInventoryList.get(i).getQuantity());

        }

        // Save changes
        inventoryRepository.saveAll(existingInventory);

        return ResponseEntity.ok("Inventory updated successfully");
    }



}


