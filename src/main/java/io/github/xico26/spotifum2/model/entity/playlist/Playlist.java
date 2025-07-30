package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="playlist")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    protected String name;

    @ManyToMany
    @JoinTable(name="playlist_music",
            joinColumns = @JoinColumn(name="playlist_id"),
            inverseJoinColumns = @JoinColumn(name="music_id"))
    protected List<Music> musics;

    @Column(name="is_public")
    protected boolean isPublic;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    protected User creator;

    /**
     * Construtor por omissão.
     */
    public Playlist() {
        this.name = "";
        this.musics = new ArrayList<Music>();
        this.isPublic = false;
    }

    public Playlist(String name, User creator) {
        this.name = name;
        this.musics = new ArrayList<Music>();
        this.isPublic = false;
        this.creator = creator;
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a copiar
     */
    public Playlist (Playlist p) {
        this.name = p.getName();
        this.musics = p.getMusics();
        this.isPublic = p.isPublic();
        this.creator = p.getCreator();
    }

    /**
     * Devolve o nome da playlist.
     * @return nome
     */
    public String getName() {
        return this.name;
    }

    /**
     * Devolve as músicas da playlist.
     * @return músicas
     */
    public List<Music> getMusics() {
        return this.musics;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setMusics(List<Music> ms) {
        this.musics = new ArrayList<>(ms);
    }

    /**
     * Adiciona música à playlist.
     * @param music música a adicionar
     */
    public void adicionarMusica(Music music) {
        this.musics.add(music);
    }

    /**
     * Altera visibilidade da playlist.
     * @param isPublic nova visibilidade (true / false)
     */
    public void setIsPublic (boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Diz se a playlist é pública ou não.
     * @return true / false
     */
    public boolean isPublic() {
        return this.isPublic;
    }

    /**
     * Devolve o criador da playlist.
     * @return utilizador
     */
    public User getCreator() {
        return this.creator;
    }

    /**
     * (Metodo Abstrato) clonagem de playlists.
     * @return
     */
    public abstract Playlist clone ();

    /**
     * Calcula o hash code de uma playlist.
     * @return hash code
     */
    public int hashCode() {
        return (int) (this.name.hashCode() + this.musics.hashCode()) * 17;
    }

    /**
     * Implementa igualdade entre playlists
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
        Playlist p = (Playlist) o;
        return (this.name.equals(p.getName())) && (this.isPublic == p.isPublic) && (this.musics.equals(p.getMusics()));
    }

    /**
     * Devolve representação em String de uma playlist.
     * @return nome da playlist - criador
     */
    public String toString() {
        return this.name + " - Criada por: " + this.creator.getName();
    }
}
