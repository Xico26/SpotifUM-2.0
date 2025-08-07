package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Album;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;

import java.util.List;

public interface AlbumDAO {
    Album findById(int id);

    List<Album> findAll();

    void save(Album a);

    void delete(Album a);

    void update(Album a);

    List<Album> findByTitle(String title);

    List<Album> findByArtist(String artist);
}
