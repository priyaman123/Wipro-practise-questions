package com.example.userservice.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "songs")
public class Song {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String singer;
    private String musicDirector;
    private String album;
    private LocalDate releaseDate;
    private boolean visible = true;

    public Song() {}

    public Song(String name, String singer, String musicDirector, String album, LocalDate releaseDate) {
        this.name = name;
        this.singer = singer;
        this.musicDirector = musicDirector;
        this.album = album;
        this.releaseDate = releaseDate;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSinger() { return singer; }
    public void setSinger(String singer) { this.singer = singer; }
    public String getMusicDirector() { return musicDirector; }
    public void setMusicDirector(String musicDirector) { this.musicDirector = musicDirector; }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
