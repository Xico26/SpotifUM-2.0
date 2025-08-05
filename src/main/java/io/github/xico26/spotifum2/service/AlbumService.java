package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.AlbumDAO;
import io.github.xico26.spotifum2.exceptions.AlbumNotFoundException;
import io.github.xico26.spotifum2.exceptions.MusicAlreadySavedException;
import io.github.xico26.spotifum2.model.entity.Album;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.util.List;

public class AlbumService {
    private final AlbumDAO albumDAO;

    public AlbumService(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    public Album findById(int id) {
        return albumDAO.findById(id);
    }

    public List<Album> findAll() {
        return albumDAO.findAll();
    }

    public boolean hasMusic(int albumId, int musicId) throws AlbumNotFoundException {
        Album album = albumDAO.findById(albumId);
        if (album == null) {
            throw new AlbumNotFoundException("Album with id " + albumId + " not found!");
        }

        if (album.getMusics() == null) {
            return false;
        }

        return album.getMusics().stream().anyMatch(m -> m.getId() == musicId);
    }

    public void addMusic (int albumId, Music m) {
        Album album = albumDAO.findById(albumId);
        if (album == null) {
            throw new AlbumNotFoundException("Album with id " + albumId + " not found!");
        }

        if (album.getMusics().contains(m)) {
            throw new MusicAlreadySavedException("Music with id " + m.getId() + " already exists in the album!");
        }

        album.getMusics().add(m);
        albumDAO.update(album);
    }

    public void removeMusic (int albumId, int musicId) {
        if (!hasMusic(albumId, musicId)) {
            return;
        }

        Album album = albumDAO.findById(albumId);
        album.getMusics().removeIf(m -> m.getId() == musicId);
        albumDAO.update(album);
    }
}
