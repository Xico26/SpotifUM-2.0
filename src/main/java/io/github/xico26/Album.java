package io.github.xico26;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representação de um álbum
 */
public class Album implements Serializable {
    private String nome;
    private String interprete;
    private String editora;
    private int anoLancamento;
    private int duracao;
    private Map<String,Musica> musicas;

    /**
     * Construtor por omissão.
     */
    public Album () {
        this.nome = "";
        this.interprete = "";
        this.editora = "";
        this.anoLancamento = 0;
        this.duracao = 0;
        this.musicas = new HashMap<String,Musica>();
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param tituloAlbum Nome do Álbum
     * @param artista Artista
     * @param editora Editora
     * @param anoLancamento Ano de Lançamento
     */
    public Album(String tituloAlbum, String artista, String editora, int anoLancamento) {
        this.nome = tituloAlbum;
        this.interprete = artista;
        this.editora = editora;
        this.anoLancamento = anoLancamento;
        this.duracao = 0;
        this.musicas = new HashMap<String,Musica>();
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param tituloAlbum Nome do Álbum
     * @param artista Artista
     * @param editora Editora
     * @param anoLancamento Ano de Lançamento
     * @param duracao Duração
     * @param musicas Lista de Músicas
     */
    public Album(String tituloAlbum, String artista, String editora, int anoLancamento, int duracao, Map<String,Musica> musicas) {
        this.nome = tituloAlbum;
        this.interprete = artista;
        this.editora = editora;
        this.anoLancamento = anoLancamento;
        this.duracao = duracao;
        setMusicas(musicas);
    }


    /**
     * Construtor de cópia.
     * @param a Album
     */
    public Album (Album a) {
        this.nome = a.getNome();
        this.interprete = a.getInterprete();
        this.editora = a.getEditora();
        this.anoLancamento = a.getAnoLancamento();
        this.duracao = a.getDuracao();
        this.musicas = a.getMusicas();
    }

    /**
     * Devolve o nome do Álbum
     * @return nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Atualiza o nome.
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Devolve o intérprete.
     * @return interprete
     */
    public String getInterprete() {
        return this.interprete;
    }

    /**
     * Atualiza o intérprete
     * @param interprete novo intérprete
     */
    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    /**
     * Devolve a editora
     * @return editora
     */
    public String getEditora() {
        return this.editora;
    }

    /**
     * Atualiza a editora
     * @param editora nova editora
     */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
     * Devolve o ano de lançamento
     * @return anoLancamento
     */
    public int getAnoLancamento() {
        return this.anoLancamento;
    }

    /**
     * Atualiza o ano de lançamento
     * @param anoLancamento novo ano de lançamento
     */
    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    /**
     * Devolve a duração do álbum
     * @return duracao
     */
    public int getDuracao() {
        return this.duracao;
    }

    /**
     * Atualiza a duração do álbum
     * @param duracao nova duração
     */
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    /**
     * Devolve a lista de músicas do álbum, efetuando clone para evitar modificação da lista original.
     * @return musicas
     */
    public Map<String, Musica> getMusicas() {
        Map<String,Musica> musicasClone = new HashMap<String,Musica>();
        for (Map.Entry<String,Musica> m : this.musicas.entrySet()) {
            musicasClone.put(m.getKey(), m.getValue().clone());
        }
        return musicasClone;
    }

    /**
     * Atualiza a lista de músicas, dada uma lista de músicas. É realizado clone das músicas.
     * @param cs lista de músicas novas
     */
    public void setMusicas(Map<String, Musica> cs) {
        this.musicas = new HashMap<String,Musica>();
        for (Map.Entry<String,Musica> c : cs.entrySet()) {
            this.musicas.put(c.getKey(), c.getValue().clone());
        }
    }

    /**
     * Adiciona música ao álbum
     * @param m música
     */
    public void adicionaMusica (Musica m) {
        this.musicas.put(m.getNome(),m.clone());
    }

    /**
     * Diz se um álbum contém ou não uma música
     * @param nome nome da música
     * @return true / false
     */
    public boolean temMusica (String nome) {
        return this.musicas.containsKey(nome);
    }

    /**
     * Remove uma música do álbum
     * @param nome nome da música
     */
    public void removeMusica (String nome) {
        if (!this.musicas.containsKey(nome)) {
            throw new MusicaNaoExisteException(nome);
        }
        this.musicas.remove(nome);
    }

    /**
     * Devolve o número de músicas no álbum
     * @return numMusicas
     */
    public int getNumMusicas() {
        return this.musicas.size();
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
        return (int) (this.nome.hashCode() + this.interprete.hashCode() + this.editora.hashCode() + (this.anoLancamento * 13) + (this.duracao * 7) + this.musicas.size());
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
        return (this.nome.equals(a.nome)) && (this.interprete.equals(a.interprete)) && (this.editora.equals(a.editora)) && this.anoLancamento == a.anoLancamento && this.duracao == a.duracao && this.musicas.equals(a.getMusicas());
    }

    /**
     * Devolve a representação em String de um álbum
     * @return String com nome, intérprete e ano de lançamento
     */
    public String toString() {
        return this.nome + " - " + this.interprete + " - " + this.anoLancamento;

    }
}
