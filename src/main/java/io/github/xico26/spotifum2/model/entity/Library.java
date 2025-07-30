package io.github.xico26.spotifum2.model.entity;

import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="library")
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name="library_saved_albums",
            joinColumns = @JoinColumn(name="library_id"),
            inverseJoinColumns = @JoinColumn(name="album_id"))
    private Set<Album> savedAlbums;

    @ManyToMany
    @JoinTable(name = "library_saved_playlists",
            joinColumns = @JoinColumn(name = "library_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> savedPlaylists;

    @ManyToMany
    @JoinTable(name = "library_saved_musics",
            joinColumns = @JoinColumn(name = "library_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id"))
    private Set<Music> savedMusics;

    // Empty constructor
    public Library() {
        this.savedAlbums = new HashSet<Album>();
        this.savedPlaylists = new HashSet<Playlist>();
        this.savedMusics = new HashSet<Music>();
    }

    // Copy constructor
    public Library(Library b) {
        this.savedAlbums = new HashSet<Album>(b.getAlbums());
        this.savedPlaylists = new HashSet<Playlist>(b.getPlaylists());
        this.savedMusics = new HashSet<Music>(b.getMusicas());
    }

    // Constructor with user
    public Library (User user) {
        this();
        this.user = user;
    }

    public int getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Album> getAlbums() {
        return new HashSet<>(this.savedAlbums);
    }

    public void setAlbums(Set<Album> albums) {
        this.savedAlbums = new HashSet<>(albums);
    }

    public Set<Music> getMusics() {
        return new HashSet<>(this.savedMusics);
    }

    public void setMusics(Set<Music> musics) {
        this.savedMusics = new HashSet<>(musics);
    }

    public Set<Playlist> getPlaylists() {
        return new HashSet<>(this.savedPlaylists);
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.savedPlaylists = new HashSet<>(playlists);
    }

    public void addAlbum(Album album) {
        this.savedAlbums.add(album);
    }

    public void removeAlbum(Album album) {
        this.savedAlbums.remove(album);
    }

    public void addMusic(Music music) {
        this.savedMusics.add(music);
    }

    public void removeMusic(Music music) {
        this.savedMusics.remove(music);
    }

    public void addPlaylist(Playlist playlist) {
        this.savedPlaylists.add(playlist);
    }

    public void removePlaylist(Playlist playlist) {
        this.savedPlaylists.remove(playlist);
    }


    /**
     * Calcula o hash code para uma biblioteca.
     * @return hash code
     */
    public int hashCode() {
        return this.savedAlbums.hashCode() * 5 + this.savedPlaylists.hashCode() * 3;
    }

    /**
     * Implementa igualdade entre bibliotecas.
     * @param o objeto
     * @return true / false
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Library b = (Library) o;
        return (this.savedAlbums.equals(b.getAlbums())) && this.savedPlaylists.equals(b.getPlaylists());
    }

    /**
     * Clona uma biblioteca usando o construtor de cópia.
     * @return biblioteca clonada
     */
    public Library clone() {
        return new Library(this);
    }

    /**
     * Representação em string de uma biblioteca.
     * @return Lista de músicas, playlists e álbuns
     */
    public String toString() {
        return "MUSICAS\n" + this.savedMusics.toString() + "\n\n" + "PLAYLISTS\n" + this.savedPlaylists.toString() + "\n\n" + "ALBUNS\n" + this.savedAlbums.toString();
    }
}
