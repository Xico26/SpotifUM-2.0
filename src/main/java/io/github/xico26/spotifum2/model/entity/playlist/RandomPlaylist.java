package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

/**
 * Playlist com músicas aleatórias. Tipo de playlist.
 */
@Entity
@DiscriminatorValue("random")
public class RandomPlaylist extends Playlist {
    public RandomPlaylist() {
        super();
    }

    public RandomPlaylist(String name, User creator) {
        super (name, creator);
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a ser copiada
     */
    public RandomPlaylist(RandomPlaylist p) {
        super (p);
    }

    /**
     * Clona uma playlist aleatória usando o constutor de cópia.
     * @return playlist clonada
     */
    public RandomPlaylist clone() {
        return new RandomPlaylist(this);
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
        RandomPlaylist p = (RandomPlaylist) o;
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
