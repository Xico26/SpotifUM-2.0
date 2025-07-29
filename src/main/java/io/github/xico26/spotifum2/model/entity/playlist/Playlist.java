package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.io.Serializable;
import java.util.*;

/**
 * Classe abstrata que implementa uma playlist, uma lista de músicas com um nome associadas a um utilizador. Podem ser públicas ou não.
 */
public abstract class Playlist implements Serializable {
    protected String nome;
    protected Map<String, Music> musicas;
    protected boolean isPublic;
    protected User criador;

    /**
     * Construtor por omissão.
     */
    public Playlist() {
        this.nome = "";
        this.musicas = new HashMap<String, Music>();
        this.isPublic = false;
        this.criador = null;
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param nome nome da playlist
     * @param criador utilizador que cria a playlist
     */
    public Playlist(String nome, User criador) {
        this.nome = nome;
        this.musicas = new HashMap<String, Music>();
        this.isPublic = false;
        this.criador = criador;
    }

    /**
     * Construtor de cópia. Aceita:
     * @param p playlist a copiar
     */
    public Playlist (Playlist p) {
        this.nome = p.getNome();
        this.musicas = p.getMusicas();
        this.isPublic = p.isPublic();
        this.criador = p.getCriador();
    }

    /**
     * Devolve o nome da playlist.
     * @return nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Devolve as músicas da playlist.
     * @return músicas
     */
    public Map<String, Music> getMusicas() {
        Map<String, Music> musicasClone = new HashMap<String, Music>();
        for (Map.Entry<String, Music> m : this.musicas.entrySet()) {
            musicasClone.put(m.getKey(), m.getValue());
        }
        return musicasClone;
    }

    /**
     * Atualiza o nome da playlist.
     * @param nome novo nome
     */
    public void setNome (String nome) {
        this.nome = nome;
    }

    /**
     * Atualiza as músicas da playlist.
     * @param cs novas músicas.
     */
    public void setMusicas(Map<String, Music> cs) {
        this.musicas = new HashMap<String, Music>();
        for (Map.Entry<String, Music> c : cs.entrySet()) {
            this.musicas.put(c.getKey(), c.getValue().clone());
        }
    }

    /**
     * Adiciona música à playlist.
     * @param music música a adicionar
     */
    public void adicionarMusica(Music music) {
        this.musicas.put(music.getTitle(), music);
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
    public User getCriador() {
        return this.criador;
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
        return (int) (this.nome.hashCode() + this.musicas.hashCode()) * 17;
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
        return (this.nome.equals(p.getNome())) && (this.isPublic == p.isPublic) && (this.musicas.equals(p.getMusicas()));
    }

    /**
     * Devolve representação em String de uma playlist.
     * @return nome da playlist - criador
     */
    public String toString() {
        return this.nome + " - Criada por: " + this.criador.getName();
    }
}
