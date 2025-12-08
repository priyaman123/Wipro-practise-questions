package com.example.userservice.service;

import com.example.userservice.dto.UserRegistrationRequest;
import com.example.userservice.entity.User;
import com.example.userservice.exception.BadRequestException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(UserRegistrationRequest req) {
        repo.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new BadRequestException("Email already registered");
        });
        User user = new User(req.getName(), req.getEmail(), req.getPassword(), req.getPhone());
        return repo.save(user);
    }

    public User validateCredentials(String email, String password) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid credentials");
        }
        return user;
    }

    public List<User> getAll() {
        return repo.findAll();
    }
}
