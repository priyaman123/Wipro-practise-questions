package com.example.userservice.controller;

import com.example.userservice.dto.PlaylistRequest;
import com.example.userservice.entity.Playlist;
import com.example.userservice.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Playlist> create(@RequestBody PlaylistRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSong(@PathVariable Long playlistId, @PathVariable Long songId) {
        return ResponseEntity.ok(service.addSong(playlistId, songId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getForUser(userId));
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<Map<String,Object>> play(@PathVariable Long id) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", "playing");
        map.put("playlistId", id);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<Map<String,Object>> stop(@PathVariable Long id) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", "stopped");
        map.put("playlistId", id);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/{id}/repeat")
    public ResponseEntity<Map<String,Object>> repeat(@PathVariable Long id) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", "repeat");
        map.put("playlistId", id);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/{id}/shuffle")
    public ResponseEntity<Map<String,Object>> shuffle(@PathVariable Long id) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", "shuffle");
        map.put("playlistId", id);
        return ResponseEntity.ok(map);
    }
}
