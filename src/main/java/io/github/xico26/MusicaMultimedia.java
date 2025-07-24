package io.github.xico26;

import java.io.Serializable;

/**
 * Musica multimédia, com um vídeo. Tipo de música.
 */
public class MusicaMultimedia extends Musica implements IMusicaMultimedia, Serializable {
    private String video;

    /**
     * Construtor por omissão.
     */
    public MusicaMultimedia() {
        super();
        this.video = "";
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param m música
     */
    public MusicaMultimedia(Musica m) {
        super(m);
        this.video = "";
    }

    /**
     * Atualiza o vídeo da música.
     * @param video novo vídeo
     */
    public void setVideo(String video) {
        this.video = video;
    }

    /**
     * Devolve o vídeo da música
     * @return vídeo
     */
    public String getVideo() {
        return this.video;
    }

    /**
     * Diz se a música é multimédia
     * @return true
     */
    public boolean isMultimedia() {
        return true;
    }

    /**
     * Representação em String de uma música multimédia
     * @return (MÚSICA MULTIMÉDIA) - [música]
     */
    public String toString() {
        return "(MÚSICA MULTIMÉDIA) " + super.toString();
    }

    /**
     * Calcula o hash code de uma música multimédia
     * @return hash code
     */
    public int hashCode() {
        return super.hashCode() * 5;
    }

    /**
     * Implementa igualdade entre músicas multimédia
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
        MusicaMultimedia m = (MusicaMultimedia) o;
        return super.equals(m) && this.video.equals(m.video) && m.isMultimedia() == this.isMultimedia();
    }

    /**
     * Clona uma música multimédia usando o construtor de cópia
     * @return música clonada
     */
    public MusicaMultimedia clone() {
        return new MusicaMultimedia(this);
    }
}
