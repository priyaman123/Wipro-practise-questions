package com.music.admin.service;

import com.music.admin.dto.AdminLoginRequest;
import com.music.admin.dto.PlaylistDto;
import com.music.admin.dto.UserDto;
import com.music.admin.entity.Admin;
import com.music.admin.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private static final String USER_SERVICE_BASE_URL = "http://localhost:8081";

    private final AdminRepository adminRepository;
    private final RestTemplate restTemplate;

    public AdminService(AdminRepository adminRepository, RestTemplate restTemplate) {
        this.adminRepository = adminRepository;
        this.restTemplate = restTemplate;
    }

    // -------- ADMIN LOGIN (VALIDATE AGAINST H2 DB) --------
    public boolean login(AdminLoginRequest request) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(request.getUsername());
        if (adminOpt.isEmpty()) {
            return false;
        }
        Admin admin = adminOpt.get();
        return admin.getPassword() != null && admin.getPassword().equals(request.getPassword());
    }

    // -------- USERS FROM USER-SERVICE --------
    public List<UserDto> users() {
        UserDto[] arr = restTemplate.getForObject(
                USER_SERVICE_BASE_URL + "/users",
                UserDto[].class
        );
        if (arr == null) {
            return List.of();
        }
        return Arrays.asList(arr);
    }

    // -------- PLAYLISTS FROM USER-SERVICE --------
    public List<PlaylistDto> playlists() {
        PlaylistDto[] arr = restTemplate.getForObject(
                USER_SERVICE_BASE_URL + "/playlists",
                PlaylistDto[].class
        );
        if (arr == null) {
            return List.of();
        }
        return Arrays.asList(arr);
    }

    // -------- SONGS THROUGH USER-SERVICE (NOT ADMIN DB) --------
    public Object addSong(Object songRequest) {
        return restTemplate.postForObject(
                USER_SERVICE_BASE_URL + "/songs",
                songRequest,
                Object.class
        );
    }

    public Object[] songs() {
        Object[] arr = restTemplate.getForObject(
                USER_SERVICE_BASE_URL + "/songs",
                Object[].class
        );
        if (arr == null) {
            return new Object[0];
        }
        return arr;
    }
}
