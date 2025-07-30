package io.github.xico26.spotifum2.model.entity.music;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EXPLICIT")
public class ExplicitMusic extends Music implements IExplicitMusic {
    /**
     * Construtor por omissão.
     */
    public ExplicitMusic() {
        super();
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param m Música
     */
    public ExplicitMusic(Music m) {
        super(m);
    }

    @Override
    public boolean isExplicit() {
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
        ExplicitMusic m = (ExplicitMusic) o;
        return super.equals(m) && this.isExplicit() == m.isExplicit();
    }

    /**
     * Clona uma música explícita usando o construtor de cópia.
     * @return música clonada
     */
    public ExplicitMusic clone() {
        return new ExplicitMusic(this);
    }
}
