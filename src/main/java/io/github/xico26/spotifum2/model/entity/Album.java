package io.github.xico26.spotifum2.model.entity;

import io.github.xico26.spotifum2.model.entity.music.Music;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="album")
public class Album {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="label")
    private String label;

    @Column(name="year", nullable=false)
    private int year;

    @Column(name="duration")
    private int duration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Music> musics = new ArrayList<>();


    // Empty Constructor
    public Album () {
        this.name = "";
        this.label = "";
        this.year = 2000;
        this.duration = 0;
        this.musics =  new ArrayList<Music>();
    }

    // Param Constructor
    public Album(String name, String label, int year, Artist artist) {
        this.name = name;
        this.label = label;
        this.year = year;
        this.duration = 0;
        this.artist = artist;
        this.musics = new ArrayList<Music>();
    }

    // Copy constructor
    public Album (Album a) {
        this.name = a.getName();
        this.label = a.getLabel();
        this.year = a.getYear();
        this.duration = a.getDuration();
        this.artist = a.getArtist();
        setMusics(a.getMusics());
    }

    /**
     * Devolve o nome do Álbum
     * @return nome
     */
    public String getName() {
        return this.name;
    }

    /**
     * Atualiza o nome.
     * @param nome novo nome
     */
    public void setName(String nome) {
        this.name = nome;
    }

    /**
     * Devolve a editora
     * @return editora
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Atualiza a editora
     * @param editora nova editora
     */
    public void setLabel(String editora) {
        this.label = editora;
    }

    /**
     * Devolve o ano de lançamento
     * @return anoLancamento
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Atualiza o ano de lançamento
     * @param anoLancamento novo ano de lançamento
     */
    public void setYear(int anoLancamento) {
        this.year = anoLancamento;
    }

    /**
     * Devolve a duração do álbum
     * @return duracao
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Atualiza a duração do álbum
     * @param duracao nova duração
     */
    public void setDuration(int duracao) {
        this.duration = duracao;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist a) {
        this.artist = a;
    }

    public List<Music> getMusics() {
        return this.musics;
    }

    public void setMusics(List<Music> ms) {
        this.musics = new ArrayList<Music>(ms);
    }

    /**
     * Faz uma cópia de um Álbum usando o construtor de cópia.
     * @return álbum
     */
    public Album clone() {
        return new Album(this);
    }

    /**
     * Calcula o hashCode para álbuns
     * @return hashCode
     */
    public int hashCode() {
        return (int) (this.name.hashCode() + this.artist.hashCode() + this.label.hashCode() + (this.year * 13));
    }

    /**
     * Implementa a igualdade entre álbuns
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
        Album a = (Album) o;
        return (this.name.equals(a.name)) && (this.artist.equals(a.artist)) && (this.label.equals(a.label)) && this.year == a.year && this.duration == a.duration;
    }

    /**
     * Devolve a representação em String de um álbum
     * @return String com nome, intérprete e ano de lançamento
     */
    public String toString() {
        return this.name + " - " + this.artist + " - " + this.year;

    }
}
