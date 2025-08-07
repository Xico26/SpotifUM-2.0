package io.github.xico26.spotifum2.dao;

import java.util.List;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;

public interface PlaylistDAO {
    Playlist findById(int id);

    List<Playlist> findAll();

    void save(Playlist playlist);

    void update(Playlist playlist);

    void delete(Playlist playlist);

    List<Playlist> findByUser(User user);

    List<Playlist> findPublicPlaylists();

    List<Playlist> findByTitle(String title);

    List<Playlist> findAllWithMusic(Music music);

}
