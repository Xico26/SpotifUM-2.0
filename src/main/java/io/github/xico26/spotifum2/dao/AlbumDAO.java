package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Album;

import java.util.List;

public interface AlbumDAO {
    Album findById(int id);

    List<Album> findAll();

    void save(Album a);

    void delete(Album a);

    void update(Album a);
}
