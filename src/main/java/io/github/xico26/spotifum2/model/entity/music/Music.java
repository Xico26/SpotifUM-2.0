package io.github.xico26.spotifum2.model.entity.music;

import io.github.xico26.spotifum2.model.entity.Artist;
import io.github.xico26.spotifum2.model.entity.Album;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name="music")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, name="title", nullable = false)
    private String title;

    @Column(name="genre")
    private String genre;

    @Column(name="lyrics", columnDefinition = "TEXT")
    private String lyrics;

    @Column(name="duration")
    private int duration;

    @Column(name="num_plays")
    private int numPlays;

    @ManyToOne(optional = false)
    @JoinColumn(name="album_id",  nullable = false)
    private Album album;


    /**
     * Default empty constructor
     */
    public Music() {
        this.title = "";
        this.genre = "";
        this.lyrics = "";
        this.duration = 0;
        this.numPlays = 0;
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param title Nome da música
     * @param genre Género
     * @param lyrics Letra (Array de linhas)
     * @param duration Duração
     */
    public Music(String title, String genre, String lyrics, int duration, Album album) {
        this.title = title;
        this.genre = genre;
        this.lyrics = lyrics;
        this.duration = duration;
        this.numPlays = 0;
        this.album = album;
    }

    /**
     * Construtor de cópia. Aceita outra música.
     * @param m música
     */
    public Music(Music m) {
        this.title = m.getTitle();
        this.genre = m.getGenre();
        this.lyrics = m.getLyrics();
        this.duration = m.getDuration();
        this.numPlays = m.getNumPlays();
        this.album = m.getAlbum();
    }

    /**
     * Devolve o nome
     * @return nome
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Devolve o género da música
     * @return género da música
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * Devolve a letra da música
     * @return letra da música
     */
    public String getLyrics() {
        return this.lyrics;
    }

    /**
     * Devolve a duração da música
     * @return duração da música
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Devolve o nº de vezes ouvida
     * @return nº de vezes ouvida
     */
    public int getNumPlays() {
        return this.numPlays;
    }


    /**
     * Atualiza o nome
     * @param nome novo nome
     */
    public void setTitle(String nome) {
        this.title = nome;
    }

    /**
     * Atualiza o género da música
     * @param genero novo género
     */
    public void setGenre(String genero) {
        this.genre = genero;
    }

    /**
     * Atualiza a letra da música.
     * @param lyrics nova letra
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Atualiza a duração da música.
     * @param duracao nova duração
     */
    public void setDuration(int duracao) {
        this.duration = duracao;
    }

    /**
     * Atualiza o número de reproduções.
     * @param numReproducoes número de reproduções.
     */
    public void setNumPlays(int numReproducoes) {
        this.numPlays = numReproducoes;
    }

    public Artist getArtist() {
        return this.album.getArtist();
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Regista uma reprodução da música, incrementando o contador.
     */
    public void registaReproducao() {
        this.numPlays++;
    }

    /**
     * Implementa a reprodução de uma música individual, imprimindo a letra.
     * @param u utilizador que ouve a música
     * @return letra da música
     */
//    public String reproduzMusica (User u) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(this.toString());
//        sb.append("\n");
//        for (String l: this.lyrics) {
//            sb.append(l);
//            sb.append("\n");
//        }
//        this.registaReproducao();
//        return sb.toString();
//    }

    /**
     * Diz se uma música é explícita ou não.
     * @return false
     */
    public boolean isExplicit() {
        return false;
    }

    /**
     * Implementa igualdade entre músicas.
     * @param o objeto
     * @return true / false
     */
    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Music m = (Music) o;
        return this.id == m.id && (this.title.equals(m.title)) && (this.album.equals(m.album)) && (this.genre.equals(m.genre));
    }

    /**
     * Representação em String de uma música.
     * @return nome - intérprete - género - editora
     */
    @Override
    public String toString () {
        return this.title + " - " + this.getArtist() + " - " + this.genre;
    }

    /**
     * Clona uma música usando o construtor de cópia.
     * @return música clonada
     */
    @Override
    public Music clone () {
        return new Music(this);
    }

    /**
     * Calcula o hash code de uma música.
     * @return hash code
     */
    @Override
    public int hashCode () {
        return this.id * 17 + this.title.hashCode() + this.getArtist().hashCode() + this.genre.hashCode() + this.lyrics.hashCode() + this.duration * 5 + this.numPlays * 3;
    }
}
