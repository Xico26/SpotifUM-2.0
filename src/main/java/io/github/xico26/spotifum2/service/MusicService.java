package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.MusicDAO;
import io.github.xico26.spotifum2.model.entity.Artist;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.util.List;

public class MusicService {
    private final MusicDAO musicDAO;

    public MusicService(MusicDAO musicDAO) {
        this.musicDAO = musicDAO;
    }

    public Music findById(int id) {
        return musicDAO.findById(id);
    }

    public List<Music> findAll() {
        return musicDAO.findAll();
    }

    public void save(Music music) {
        musicDAO.save(music);
    }

    public void delete(Music music) {
        musicDAO.delete(music);
    }

    public int getTotalNumberOfMusics() {
        return musicDAO.getMusicCount();
    }

    public List<Music> searchByTitle(String title) {
        return musicDAO.findByTitle(title);
    }

    public List<Music> searchByGenre(String genre) {
        return musicDAO.getMusicsByGenre(genre);
    }
}

