package com.example.userservice.service;

import com.example.userservice.dto.SongAdminRequest;
import com.example.userservice.entity.Song;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository repo;

    public SongService(SongRepository repo) {
        this.repo = repo;
    }

    public List<Song> getAllVisible() {
        return repo.findAll().stream().filter(Song::isVisible).toList();
    }

    public Song createFromAdmin(SongAdminRequest req) {
        Song song = new Song(req.getName(), req.getSinger(), req.getMusicDirector(),
                req.getAlbum(), req.getReleaseDate());
        return repo.save(song);
    }

    public Song get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
    }
}
