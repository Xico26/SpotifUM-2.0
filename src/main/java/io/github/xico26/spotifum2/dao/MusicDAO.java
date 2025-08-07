package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Artist;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.util.List;

public interface MusicDAO {
    Music findById(int id);

    List<Music> findAll();

    void save(Music music);

    void delete(Music music);

    int getMusicCount();

    List<Music> findByTitle(String title);

    List<Music> getMusicsByGenre(String genre);
}

