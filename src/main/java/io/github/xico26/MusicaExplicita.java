package io.github.xico26;

import java.io.Serializable;
import java.util.List;

/**
 * Música Explícita. Tipo de Música.
 */
public class MusicaExplicita extends Musica implements IMusicaExplicita, Serializable {
    /**
     * Construtor por omissão.
     */
    public MusicaExplicita() {
        super();
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param m Música
     */
    public MusicaExplicita(Musica m) {
        super(m);
    }

    /**
     * Diz se uma música é explícita
     * @return true
     */
    public boolean isExplicita() {
        return true;
    }

    /**
     * Representação em String de uma música explícita
     * @return (MÚSICA EXPLÍCITA) - [música]
     */
    public String toString() {
        return "(MÚSICA EXPLÍCITA) " + super.toString();
    }

    /**
     * Calcula o hash code de uma música explícita.
     * @return hash code
     */
    public int hashCode() {
        return super.hashCode() * 7;
    }

    /**
     * Implementa igualdade entre músicas explícitas
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
        MusicaExplicita m = (MusicaExplicita) o;
        return super.equals(m) && this.isExplicita() == m.isExplicita();
    }

    /**
     * Clona uma música explícita usando o construtor de cópia.
     * @return música clonada
     */
    public MusicaExplicita clone() {
        return new MusicaExplicita(this);
    }
}
