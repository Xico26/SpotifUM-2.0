package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.MusicDAO;
import io.github.xico26.spotifum2.exceptions.MusicNotFoundException;
import io.github.xico26.spotifum2.model.entity.Album;
import io.github.xico26.spotifum2.model.entity.Library;
import io.github.xico26.spotifum2.model.entity.music.ExplicitMusic;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;

import java.util.ArrayList;
import java.util.List;

public class MusicService {
    private final MusicDAO musicDAO;
    private AlbumService albumService;
    private LibraryService libraryService;
    private PlaylistService playlistService;

    public MusicService(MusicDAO musicDAO, AlbumService albumService) {
        this.musicDAO = musicDAO;
        this.albumService = albumService;
    }

    public void setAlbumService (AlbumService albumService) {
        this.albumService = albumService;
    }

    public void setLibraryService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
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
        // Remove from all playlists
        List<Playlist> playlists = playlistService.findAllWithMusic(music);
        for (Playlist p : playlists) {
            p.getMusics().remove(music);
            playlistService.save(p);
        }

        // Remove from all libraries
        List<Library> libraries = libraryService.findAllWithMusic(music);
        for (Library l : libraries) {
            l.getMusics().remove(music);
            libraryService.save(l);
        }

        // Remove from album
        if (music.getAlbum() != null) {
            Album album = music.getAlbum();
            album.getMusics().remove(music);
            albumService.save(album);
        }

        // Delete music
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

    public List<Music> searchByArtist(String artist) {
        List<Music> musics = new ArrayList<Music>();
        albumService.searchByArtist(artist).forEach(a -> musics.addAll(a.getMusics()));

        return musics;
    }

    public void makeExplicit (Music music) {
        ExplicitMusic newMusic = new ExplicitMusic(music);

        // delete old
        delete(music);

        // save new
        save(newMusic);

        // add to album
        Album album = music.getAlbum();
        album.getMusics().add(newMusic);
        albumService.save(album);
    }

    public void makeNormal (ExplicitMusic music) {
        Music newMusic = new Music(music);

        // delete old
        delete(music);

        // save new
        save(newMusic);

        // add to album
        Album album = music.getAlbum();
        album.getMusics().add(newMusic);
        albumService.save(album);
    }
}

