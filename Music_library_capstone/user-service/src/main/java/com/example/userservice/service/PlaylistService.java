package com.example.userservice.service;

import com.example.userservice.dto.PlaylistRequest;
import com.example.userservice.entity.Playlist;
import com.example.userservice.entity.Song;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.PlaylistRepository;
import com.example.userservice.repository.SongRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepo;
    private final UserRepository userRepo;
    private final SongRepository songRepo;

    public PlaylistService(PlaylistRepository playlistRepo, UserRepository userRepo, SongRepository songRepo) {
        this.playlistRepo = playlistRepo;
        this.userRepo = userRepo;
        this.songRepo = songRepo;
    }

    public Playlist create(PlaylistRequest req) {
        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Playlist playlist = new Playlist(req.getName(), user);
        return playlistRepo.save(playlist);
    }

    public Playlist addSong(Long playlistId, Long songId) {
        Playlist playlist = playlistRepo.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));
        Song song = songRepo.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));
        playlist.getSongs().add(song);
        return playlistRepo.save(playlist);
    }

    public Playlist get(Long id) {
        return playlistRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));
    }

    public List<Playlist> getForUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return playlistRepo.findByUser(user);
    }
}
