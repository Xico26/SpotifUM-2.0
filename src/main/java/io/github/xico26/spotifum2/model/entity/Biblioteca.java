package io.github.xico26.spotifum2.model.entity;

import io.github.xico26.spotifum2.exceptions.AlbumJaGuardadoException;
import io.github.xico26.spotifum2.exceptions.AlbumNaoExisteException;
import io.github.xico26.spotifum2.exceptions.MusicaJaGuardadaException;
import io.github.xico26.spotifum2.exceptions.PlaylistJaGuardadaException;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representação da Biblioteca de um utilizador, onde pode guardar músicas, álbuns e playlists.
 */
public class Biblioteca implements Serializable {
    private Map<String, Album> albunsGuardados;
    private Map<String, Playlist> playlistsGuardadas;
    private Map<String, Music> musicasGuardadas;

    /**
     * Constutor por omissão.
     */
    public Biblioteca() {
        this.albunsGuardados = new HashMap<String, Album>();
        this.playlistsGuardadas = new HashMap<String, Playlist>();
        this.musicasGuardadas = new HashMap<String, Music>();
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param albuns lista de álbuns
     * @param playlists lista de playlists
     * @param musicas lista de músicas
     */
    public Biblioteca(Map<String, Album> albuns, Map<String, Playlist> playlists, Map<String, Music> musicas) {
        setAlbuns(albuns);
        setPlaylists(playlists);
        setMusicas(musicas);
    }

    /**
     * Constutor de cópia.
     * @param b Biblioteca a copiar
     */
    public Biblioteca(Biblioteca b) {
        setAlbuns(b.getAlbuns());
        setPlaylists(b.getPlaylists());
        setMusicas(b.getMusicas());
    }

    /**
     * Devolve os álbuns guardados
     * @return lista de álbuns guardados
     */
    public Map<String, Album> getAlbuns() {
        Map<String, Album> albunsClone = new HashMap<String, Album>();
        for (Map.Entry<String, Album> a : this.albunsGuardados.entrySet()) {
            albunsClone.put(a.getKey(), a.getValue());
        }
        return albunsClone;
    }

    /**
     * Atualiza a lista de álbuns guardados.
     * @param albuns nova lista de álbuns
     */
    public void setAlbuns(Map<String, Album> albuns) {
        this.albunsGuardados = new HashMap<String, Album>();
        for (Map.Entry<String, Album> a : albuns.entrySet()) {
            this.albunsGuardados.put(a.getKey(), a.getValue().clone());
        }
    }

    /**
     * Devolve a lista de playlists guardadas.
     * @return lista de playlists guardadas.
     */
    public Map<String, Playlist> getPlaylists() {
        Map<String, Playlist> playlistsClone = new HashMap<String, Playlist>();
        for (Map.Entry<String, Playlist> p : this.playlistsGuardadas.entrySet()) {
            playlistsClone.put(p.getKey(), p.getValue());
        }
        return playlistsClone;
    }

    /**
     * Atualiza a lista de playlists guardadas.
     * @param playlists nova lista de playlists
     */
    public void setPlaylists(Map<String, Playlist> playlists) {
        this.playlistsGuardadas = new HashMap<String, Playlist>();
        for (Map.Entry<String, Playlist> p : playlists.entrySet()) {
            this.playlistsGuardadas.put(p.getKey(), p.getValue().clone());
        }
    }

    /**
     * Devolve a lista de músicas guardadas.
     * @return lista de músicas guardadas
     */
    public Map<String, Music> getMusicas() {
        Map<String, Music> musicasClone = new HashMap<String, Music>();
        for (Map.Entry<String, Music> m : this.musicasGuardadas.entrySet()) {
            musicasClone.put(m.getKey(), m.getValue());
        }
        return musicasClone;
    }

    /**
     * Atualiza a lista de músicas guardadas.
     * @param musicas nova lista de músicas
     */
    public void setMusicas(Map<String, Music> musicas) {
        this.musicasGuardadas = new HashMap<String, Music>();
        for (Map.Entry<String, Music> m : musicas.entrySet()) {
            this.musicasGuardadas.put(m.getKey(), m.getValue().clone());
        }
    }

    /**
     * Adiciona um álbum aos álbuns guardados.
     * @param album álbum a adicionar
     */
    public void adicionarAlbum(Album album) {
        if (this.albunsGuardados.containsKey(album.getName())) {
            throw new AlbumJaGuardadoException(album.getName());
        }
        this.albunsGuardados.put(album.getName(), album);
    }

    /**
     * Adiciona uma playlist às playlists guardadas.
     * @param playlist playlist a adicionar
     */
    public void adicionarPlaylist(Playlist playlist) {
        if (this.playlistsGuardadas.containsKey(playlist.getNome())) {
            throw new PlaylistJaGuardadaException(playlist.getNome());
        }
        this.playlistsGuardadas.put(playlist.getNome(), playlist);
    }

    /**
     * Remove um álbum da lista de álbuns guardados
     * @param nome Nome do álbum a remover
     * @throws AlbumNaoExisteException exceção caso álbum não exista
     */
    public void removerAlbum(String nome) throws AlbumNaoExisteException {
        if (!this.albunsGuardados.containsKey(nome)) {
            throw new AlbumNaoExisteException(nome);
        }
        this.albunsGuardados.remove(nome);
    }

    /**
     * Remove uma playlist da lista de playlists guardadas
     * @param nome Nome da playlist a remover
     */
    public void removerPlaylist(String nome) {
        this.playlistsGuardadas.remove(nome);

    }

    /**
     * Adiciona uma música às músicas guardadas
     * @param m Música a adicionar
     * @throws MusicaJaGuardadaException exceção caso já esteja guardada
     */
    public void adicionarMusica(Music m) throws MusicaJaGuardadaException {
        if (this.musicasGuardadas.containsKey(m.getTitle())) {
            throw new MusicaJaGuardadaException("Música já guardada!");
        }
        this.musicasGuardadas.put(m.getTitle(), m);
    }

    /**
     * Calcula o hash code para uma biblioteca.
     * @return hash code
     */
    public int hashCode() {
        return this.albunsGuardados.hashCode() * 5 + this.playlistsGuardadas.hashCode() * 3;
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
        Biblioteca b = (Biblioteca) o;
        return (this.albunsGuardados.equals(b.getAlbuns())) && this.playlistsGuardadas.equals(b.getPlaylists());
    }

    /**
     * Clona uma biblioteca usando o construtor de cópia.
     * @return biblioteca clonada
     */
    public Biblioteca clone() {
        return new Biblioteca(this);
    }

    /**
     * Representação em string de uma biblioteca.
     * @return Lista de músicas, playlists e álbuns
     */
    public String toString() {
        return "MUSICAS\n" + this.musicasGuardadas.toString() + "\n\n" + "PLAYLISTS\n" + this.playlistsGuardadas.toString() + "\n\n" + "ALBUNS\n" + this.albunsGuardados.toString();
    }
}
