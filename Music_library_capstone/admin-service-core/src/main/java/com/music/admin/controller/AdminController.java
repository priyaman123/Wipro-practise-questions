package com.music.admin.controller;

import com.music.admin.dto.AdminLoginRequest;
import com.music.admin.dto.PlaylistDto;
import com.music.admin.dto.UserDto;
import com.music.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // -------- LOGIN --------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        boolean ok = adminService.login(request);
        if (!ok) {
            return ResponseEntity.status(401).body("Invalid admin username or password");
        }
        // Simple success response (no JWT etc. for now)
        return ResponseEntity.ok("LOGIN_OK");
    }

    // -------- USERS --------
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> users() {
        return ResponseEntity.ok(adminService.users());
    }

    // -------- PLAYLISTS --------
    @GetMapping("/playlists")
    public ResponseEntity<List<PlaylistDto>> playlists() {
        return ResponseEntity.ok(adminService.playlists());
    }

    // -------- SONGS --------
    @GetMapping("/songs")
    public ResponseEntity<Object[]> songs() {
        return ResponseEntity.ok(adminService.songs());
    }

    @PostMapping("/songs")
    public ResponseEntity<Object> addSong(@RequestBody Object songRequest) {
        Object created = adminService.addSong(songRequest);
        return ResponseEntity.ok(created);
    }
}
