package com.example.userservice.dto;

import java.time.LocalDate;

public class SongAdminRequest {

    private String name;
    private String singer;
    private String musicDirector;
    private String album;
    private LocalDate releaseDate;

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
}
