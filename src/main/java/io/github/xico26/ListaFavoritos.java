package io.github.xico26;

import java.io.Serializable;

/**
 * Lista com músicas mais ouvidas de um utilizador. Tipo de playlist.
 */
public class ListaFavoritos extends Playlist implements Serializable {
    /**
     * Construtor parametrizado. Aceita:
     * @param nome nome da playlist
     * @param criador utilizador que cria a playlist
     */
    public ListaFavoritos(String nome, Utilizador criador) {
        super (nome, criador);
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a copiar
     */
    public ListaFavoritos (ListaFavoritos p) {
        super (p);
    }

    /**
     * Clona uma lista de favoritos usando o construtor de cópia
     * @return lista clonada
     */
    public ListaFavoritos clone() {
        return new ListaFavoritos(this);
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
        ListaFavoritos f = (ListaFavoritos) o;
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
