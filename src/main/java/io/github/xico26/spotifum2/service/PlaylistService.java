package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.PlaylistDAO;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;

import java.util.List;

public class PlaylistService {
    private final PlaylistDAO playlistDAO;

    public PlaylistService(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public Playlist findById(int id) {
        return playlistDAO.findById(id);
    }

    public List<Playlist> findByUser(User user) {
        return playlistDAO.findByUser(user);
    }

    public List<Playlist> findPublicPlaylists () {
        return playlistDAO.findPublicPlaylists();
    }

    public List<Playlist> findAll() {
        return playlistDAO.findAll();
    }

    public void save(Playlist playlist) {
        playlistDAO.save(playlist);
    }

    public void delete(Playlist playlist) {
        playlistDAO.delete(playlist);
    }

    public void update(Playlist playlist) {
        playlistDAO.update(playlist);
    }
}
