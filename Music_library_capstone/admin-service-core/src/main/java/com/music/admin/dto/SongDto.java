package com.music.admin.dto;

public class SongDto {
    private Long id;
    private String name;
    private String singer;
    private String album;
    private String musicDirector;
    private String releaseDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSinger() { return singer; }
    public void setSinger(String singer) { this.singer = singer; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getMusicDirector() { return musicDirector; }
    public void setMusicDirector(String musicDirector) { this.musicDirector = musicDirector; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}

