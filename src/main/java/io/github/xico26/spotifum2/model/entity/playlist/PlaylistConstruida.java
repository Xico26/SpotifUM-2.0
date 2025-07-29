package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;

import java.io.Serializable;

/**
 * Playlist construida por utilizadores. Tipo de Playlist.
 */
public class PlaylistConstruida extends Playlist implements Serializable {
    /**
     * Construtor parametrizado. Aceita:
     * @param nome nome da playlist
     * @param criador criador
     */
    public PlaylistConstruida(String nome, User criador) {
        super(nome, criador);
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a ser copiada
     */
    public PlaylistConstruida (PlaylistConstruida p) {
        super (p);
    }

    /**
     * Clona uma playlist construída usando o constutor de cópia.
     * @return playlist clonada
     */
    public PlaylistConstruida clone() {
        return new PlaylistConstruida(this);
    }

    /**
     * Calcula o hash code de uma playlist construída.
     * @return hash code
     */
    public int hashCode() {
        return super.hashCode() * 23;
    }

    /**
     * Implementa igualdade entre playlists construídas.
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
        PlaylistConstruida p = (PlaylistConstruida) o;
        return super.equals(p);
    }

    /**
     * Representação em String de uma playlist construída
     * @return representação em String de uma playlist.
     */
    public String toString() {
        return super.toString();
    }
}
