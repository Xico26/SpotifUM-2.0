package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.LibraryDAO;
import io.github.xico26.spotifum2.exceptions.AlbumAlreadySavedException;
import io.github.xico26.spotifum2.exceptions.MusicAlreadySavedException;
import io.github.xico26.spotifum2.exceptions.PlaylistAlreadySavedException;
import io.github.xico26.spotifum2.model.entity.Album;
import io.github.xico26.spotifum2.model.entity.Library;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;

public class LibraryService {
    private final LibraryDAO libraryDAO;

    public LibraryService(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    public Library getUserLibrary (User u) {
        return libraryDAO.findByUser(u);
    }

    public boolean hasMusic (User u, Music m) {
        Library library = getUserLibrary(u);
        return library.getMusics().contains(m);
    }

    public void addMusic (User u, Music m) {
        Library library = getUserLibrary(u);
        if (!library.getMusics().contains(m)) {
            library.addMusic(m);
            libraryDAO.update(library);
        } else {
            throw new MusicAlreadySavedException("Music already saved!");
        }
    }

    public void removeMusic (User u, Music m) {
        Library library = getUserLibrary(u);
        if (library.getMusics().contains(m)) {
            library.removeMusic(m);
            libraryDAO.update(library);
        }
    }

    public void addAlbum(User user, Album album) {
        Library library = libraryDAO.findByUser(user);
        if (!library.getAlbums().contains(album)) {
            library.addAlbum(album);
            libraryDAO.update(library);
        } else {
            throw new AlbumAlreadySavedException("Album already saved!");
        }
    }

    public void removeAlbum(User user, Album album) {
        Library library = libraryDAO.findByUser(user);
        if (library.getAlbums().contains(album)) {
            library.removeAlbum(album);
            libraryDAO.update(library);
        }
    }

    public boolean hasAlbum(User u, Album album) {
        Library library = getUserLibrary(u);
        return library.getAlbums().contains(album);
    }

    public void addPlaylist(User user, Playlist playlist) {
        Library library = libraryDAO.findByUser(user);
        if (!library.getPlaylists().contains(playlist)) {
            library.addPlaylist(playlist);
            libraryDAO.update(library);
        } else {
            throw new PlaylistAlreadySavedException("Playlist already saved!");
        }
    }

    public void removePlaylist(User user, Playlist playlist) {
        Library library = libraryDAO.findByUser(user);
        if (library.getPlaylists().contains(playlist)) {
            library.removePlaylist(playlist);
            libraryDAO.update(library);
        }
    }

    public boolean hasPlaylist(User u, Playlist playlist) {
        Library library = getUserLibrary(u);
        return library.getPlaylists().contains(playlist);
    }

    public boolean hasPlaylistByName (User u, String name) {
        Library library = getUserLibrary(u);
        return library.getPlaylists().stream().anyMatch(p -> p.getName().equals(name));
    }

    public void removePlaylistByName (User u, String name) {
        Library library = getUserLibrary(u);
        if (hasPlaylistByName(u, name)) {
            library.removePlaylist(library.getPlaylists().stream().filter(p -> p.getName().equals(name)).findFirst().get());
            libraryDAO.update(library);
        }
    }
}
