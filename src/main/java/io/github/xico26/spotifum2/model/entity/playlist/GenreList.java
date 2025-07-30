package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("genre")
public class GenreList extends Playlist {
    public GenreList() {
        super();
    }

    public GenreList(String name, User creator) {
        super (name, creator);
    }

    public GenreList(GenreList p) {
        super(p);
    }

    /**
     * Clona uma lista usando o construtor de cópia.
     * @return lista clonada
     */
    public GenreList clone () {
        return new GenreList(this);
    }

    /**
     * Calcula o hash code para uma lista deste tipo.
     * @return hash code
     */
    public int hashCode() {
        return super.hashCode() * 13;
    }

    /**
     * Implementa igualdade entre listas deste tipo.
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
        GenreList l = (GenreList) o;
        return super.equals(l);
    }

    /**
     * Representação em String de uma lista deste tipo.
     * @return representação em String de uma playlist.
     */
    public String toString() {
        return super.toString();
    }
}
