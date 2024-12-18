package com.example.demo.controllers;

import java.util.Optional;

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

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerEntity player) {
        // Check if the username already exists
        Optional<PlayerEntity> existingPlayer = playerRepository.findByUsername(player.getUsername());
        if (existingPlayer.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        // Save the player entity (ID will be generated automatically)
        player.setId(null);
        player.setLevel(1);
        player.setCurrentExp(0);
        player.setMaxExp(100);
        player.setArmor(1);
        player.setDamage(1);
        player.setCurrentHp(10);
        player.setMaxHp(10);
        player.setDefenceStat(1);
        player.setStrengthStat(1);
        player.setGold(5);
      
        playerRepository.save(player);

        return ResponseEntity.ok("Player registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<PlayerEntity> loginPlayer(@RequestBody PlayerEntity player) {
        if (player.getUsername() == null || player.getUsername().isBlank() ||
            player.getPassword() == null || player.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password cannot be empty");
        }

        // Find player by username
        PlayerEntity dbPlayer = playerRepository.findByUsername(player.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        // Compare passwords
        if (!dbPlayer.getPassword().equals(player.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        return ResponseEntity.ok(dbPlayer);
    }

    
    @PutMapping("/update")
    public ResponseEntity<?> updatePlayer(@RequestBody PlayerEntity player) {
        if (player.getUsername() == null || player.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password cannot be null");
        }

        Optional<PlayerEntity> existingPlayerOpt = playerRepository.findByUsername(player.getUsername());

        if (existingPlayerOpt.isPresent()) {
            PlayerEntity existingPlayer = existingPlayerOpt.get();

            existingPlayer.setPassword(player.getPassword());
            existingPlayer.setLevel(player.getLevel());
            existingPlayer.setCurrentHp(player.getCurrentHp());
            existingPlayer.setMaxHp(player.getMaxHp());
            existingPlayer.setStrengthStat(player.getStrengthStat());
            existingPlayer.setDefenceStat(player.getDefenceStat());
            existingPlayer.setDamage(player.getDamage());
            existingPlayer.setArmor(player.getArmor());
            existingPlayer.setCurrentExp(player.getCurrentExp());
            existingPlayer.setMaxExp(player.getMaxExp());
            existingPlayer.setGold(player.getGold());
            existingPlayer.setStatPoints(player.getStatPoints());

            playerRepository.save(existingPlayer);
            return ResponseEntity.ok("Player data updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }


    @GetMapping("/{username}")
    public ResponseEntity<PlayerEntity> getPlayer(@PathVariable String username) {
        PlayerEntity player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        return ResponseEntity.ok(player);
    }



}


