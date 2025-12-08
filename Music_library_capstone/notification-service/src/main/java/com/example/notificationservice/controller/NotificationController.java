package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/song-added")
    public ResponseEntity<Notification> songAdded(@RequestBody Map<String,Object> payload) {
        Object songId = payload.get("songId");
        Object name = payload.get("name");
        String msg = "New song added: " + name + " (ID: " + songId + ")";
        return ResponseEntity.ok(service.create(msg));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> all() {
        return ResponseEntity.ok(service.list());
    }
}
