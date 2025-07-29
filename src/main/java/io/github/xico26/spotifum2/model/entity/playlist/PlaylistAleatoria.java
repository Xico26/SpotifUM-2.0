package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;

import java.io.Serializable;

/**
 * Playlist com músicas aleatórias. Tipo de playlist.
 */
public class PlaylistAleatoria extends Playlist implements Serializable {

    /**
     * Constutor parametrizado. Aceita:
     * @param nome nome da playlist
     * @param criador criador
     */
    public PlaylistAleatoria(String nome, User criador) {
        super (nome, criador);
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a ser copiada
     */
    public PlaylistAleatoria (PlaylistAleatoria p) {
        super (p);
    }

    /**
     * Clona uma playlist aleatória usando o constutor de cópia.
     * @return playlist clonada
     */
    public PlaylistAleatoria clone() {
        return new PlaylistAleatoria(this);
    }

    /**
     * Calcula o hash code de uma playlist aleatória.
     * @return hash code
     */
    public int hashCode() {
        return super.hashCode() * 17;
    }

    /**
     * Implementa igualdade entre playlists aleatórias.
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
        PlaylistAleatoria p = (PlaylistAleatoria) o;
        return super.equals(p);
    }

    /**
     * Representação em String de uma playlist aleatória
     * @return representação em String de uma playlist.
     */
    public String toString() {
        return super.toString();
    }
}
