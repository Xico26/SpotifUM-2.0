package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

/**
 * Playlist construida por utilizadores. Tipo de Playlist.
 */
@Entity
@DiscriminatorValue("custom")
public class CustomPlaylist extends Playlist {
    public CustomPlaylist() {
        super();
    }

    public CustomPlaylist(String name, User creator) {
        super(name, creator);
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a ser copiada
     */
    public CustomPlaylist(CustomPlaylist p) {
        super (p);
    }

    /**
     * Clona uma playlist construída usando o constutor de cópia.
     * @return playlist clonada
     */
    public CustomPlaylist clone() {
        return new CustomPlaylist(this);
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
        CustomPlaylist p = (CustomPlaylist) o;
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
