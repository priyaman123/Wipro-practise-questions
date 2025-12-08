package com.example.userservice.controller;

import com.example.userservice.dto.SongAdminRequest;
import com.example.userservice.entity.Song;
import com.example.userservice.service.SongService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService service;

    public SongController(SongService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAll() {
        return ResponseEntity.ok(service.getAllVisible());
    }

    @PostMapping("/internal/admin-create")
    public ResponseEntity<Song> createFromAdmin(@Valid @RequestBody SongAdminRequest req) {
        return ResponseEntity.ok(service.createFromAdmin(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }
    @PostMapping
    public ResponseEntity<Song> createSong(@Valid @RequestBody SongAdminRequest req) {
        return ResponseEntity.ok(service.createFromAdmin(req));
    }

}
