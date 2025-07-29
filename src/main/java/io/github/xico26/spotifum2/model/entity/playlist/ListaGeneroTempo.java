package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;

import java.io.Serializable;

/**
 * Lista com músicas de um dado género com duração inferior a um determinado valor. Tipo de playlist.
 */
public class ListaGeneroTempo extends Playlist implements Serializable {
    /**
     * Construtor parametrizado. Aceita:
     * @param nome nome da playlist
     * @param criador utilizador que cria a playlist
     */
    public ListaGeneroTempo(String nome, User criador) {
        super (nome, criador);
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a copiar
     */
    public ListaGeneroTempo(ListaGeneroTempo p) {
        super(p);
    }

    /**
     * Clona uma lista usando o construtor de cópia.
     * @return lista clonada
     */
    public ListaGeneroTempo clone () {
        return new ListaGeneroTempo(this);
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
        ListaGeneroTempo l = (ListaGeneroTempo) o;
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
