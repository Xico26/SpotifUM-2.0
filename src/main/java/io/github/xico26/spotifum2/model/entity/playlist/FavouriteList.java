package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("favourite")
public class FavouriteList extends Playlist {
    public FavouriteList() {
        super();
    }

    public FavouriteList(String name, User creator) {
        super (name, creator);
    }

    public FavouriteList(FavouriteList p) {
        super (p);
    }

    /**
     * Clona uma lista de favoritos usando o construtor de cópia
     * @return lista clonada
     */
    public FavouriteList clone() {
        return new FavouriteList(this);
    }

    /**
     * Calcula o hash code para uma lista de favoritos.
     * @return hash code
     */
    public int hashCode() {
        return super.hashCode() * 7;
    }

    /**
     * Implementa igualdade entre listas de favoritos.
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
        FavouriteList f = (FavouriteList) o;
        return super.equals(f);
    }

    /**
     * Representação em String de uma lista de favoritos.
     * @return representação em String de uma playlist.
     */
    public String toString() {
        return super.toString();
    }
}
