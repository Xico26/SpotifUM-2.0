package io.github.xico26.spotifum2.model.entity.playlist;

import io.github.xico26.spotifum2.model.entity.Utilizador;
import io.github.xico26.spotifum2.model.entity.music.Musica;

import java.io.Serializable;
import java.util.*;

/**
 * Classe abstrata que implementa uma playlist, uma lista de músicas com um nome associadas a um utilizador. Podem ser públicas ou não.
 */
public abstract class Playlist implements Serializable {
    protected String nome;
    protected Map<String, Musica> musicas;
    protected boolean isPublic;
    protected Utilizador criador;

    /**
     * Construtor por omissão.
     */
    public Playlist() {
        this.nome = "";
        this.musicas = new HashMap<String,Musica>();
        this.isPublic = false;
        this.criador = null;
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param nome nome da playlist
     * @param criador utilizador que cria a playlist
     */
    public Playlist(String nome, Utilizador criador) {
        this.nome = nome;
        this.musicas = new HashMap<String,Musica>();
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
    public Map<String, Musica> getMusicas() {
        Map<String,Musica> musicasClone = new HashMap<String,Musica>();
        for (Map.Entry<String,Musica> m : this.musicas.entrySet()) {
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
    public void setMusicas(Map<String, Musica> cs) {
        this.musicas = new HashMap<String,Musica>();
        for (Map.Entry<String,Musica> c : cs.entrySet()) {
            this.musicas.put(c.getKey(), c.getValue().clone());
        }
    }

    /**
     * Adiciona música à playlist.
     * @param musica música a adicionar
     */
    public void adicionarMusica(Musica musica) {
        this.musicas.put(musica.getNome(), musica);
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
    public Utilizador getCriador() {
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
        return this.nome + " - Criada por: " + this.criador.getNome();
    }
}
