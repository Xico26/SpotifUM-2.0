package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.AlbumDAO;
import io.github.xico26.spotifum2.exceptions.AlbumNotFoundException;
import io.github.xico26.spotifum2.exceptions.MusicAlreadySavedException;
import io.github.xico26.spotifum2.model.entity.Album;
import io.github.xico26.spotifum2.model.entity.Artist;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.util.List;

public class AlbumService {
    private final AlbumDAO albumDAO;
    private final ArtistService artistService;

    public AlbumService(AlbumDAO albumDAO, ArtistService artistService) {
        this.albumDAO = albumDAO;
        this.artistService = artistService;
    }

    public Album findById(int id) {
        return albumDAO.findById(id);
    }

    public List<Album> findAll() {
        return albumDAO.findAll();
    }

    public List<Album> searchByTitle(String title) {
        return albumDAO.findByTitle(title);
    }

    public List<Album> searchByArtist(String artist) {
        return albumDAO.findByArtist(artist);
    }

    public void save (Album album) {
        albumDAO.save(album);
    }

    public void delete (Album album) {
        albumDAO.delete(album);
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

    public void createAlbum(String title, String artistName, String label, int year) {
        Artist artist = artistService.findByName(artistName);
        Album album = new Album(title, label, year, artist);

        save(album);
        artistService.addAlbum(artist, album);
    }
}
