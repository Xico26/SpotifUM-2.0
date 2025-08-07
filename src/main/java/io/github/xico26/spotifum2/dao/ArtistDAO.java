package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Artist;

import java.util.List;

public interface ArtistDAO {
    Artist findById(int id);

    List<Artist> findAll();

    Artist findByName(String name);

    void save(Artist a);

    void delete(Artist a);

    void update(Artist a);
}
