package io.github.xico26;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Musica implements Serializable {
    private String nome;
    private String interprete;
    private String genero;
    private String editora;
    private List<String> letra;
    private List<String> musica;
    private int duracao;
    private int numReproducoes;

    /**
     * Construtor por omissão
     */
    public Musica() {
        this.nome = "";
        this.interprete = "";
        this.editora = "";
        this.letra = new ArrayList<String>();
        this.musica = new ArrayList<String>();
        this.genero = "";
        this.duracao = 0;
        this.numReproducoes = 0;
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param nome Nome da música
     * @param interprete Interprete
     * @param genero Género
     * @param editora Editora
     * @param letra Letra (Array de linhas)
     * @param musica Música (Array de linhas de caracteres musicais)
     * @param duracao Duração
     */
    public Musica(String nome, String interprete, String genero, String editora, List<String> letra, List<String> musica, int duracao) {
        this.nome = nome;
        this.interprete = interprete;
        this.genero = genero;
        this.editora = editora;
        this.setLetra(letra);
        this.setMusica(musica);
        this.duracao = duracao;
    }

    /**
     * Construtor de cópia. Aceita outra música.
     * @param m música
     */
    public Musica(Musica m) {
        this.nome = m.getNome();
        this.interprete = m.getInterprete();
        this.genero = m.getGenero();
        this.editora = m.getEditora();
        this.letra = m.getLetra();
        this.musica = m.getMusica();
        this.duracao = m.getDuracao();
        this.numReproducoes = m.getNumReproducoes();
    }

    /**
     * Devolve o nome
     * @return nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Devolve o intérprete da música
     * @return intérprete da música
     */
    public String getInterprete() {
        return this.interprete;
    }

    /**
     * Devolve o género da música
     * @return género da música
     */
    public String getGenero() {
        return this.genero;
    }

    /**
     * Devolve a editora da música
     * @return editora da música
     */
    public String getEditora() {
        return this.editora;
    }

    /**
     * Devolve a letra da música
     * @return letra da música
     */
    public List<String> getLetra() {
        return new ArrayList<String>(this.letra);
    }

    /**
     * Devolve a música
     * @return música
     */
    public List<String> getMusica() {
        return new ArrayList<String>(this.musica);
    }

    /**
     * Devolve a duração da música
     * @return duração da música
     */
    public int getDuracao() {
        return this.duracao;
    }

    /**
     * Devolve o nº de vezes ouvida
     * @return nº de vezes ouvida
     */
    public int getNumReproducoes() {
        return this.numReproducoes;
    }


    /**
     * Atualiza o nome
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Atualiza o intérprete da música
     * @param interprete novo interprete
     */
    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    /**
     * Atualiza o género da música
     * @param genero novo género
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Atualiza a editora da música.
     * @param editora nova editora.
     */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
     * Atualiza a letra da música.
     * @param letra nova letra
     */
    public void setLetra(List<String> letra) {
        this.letra = new ArrayList<String>(letra);
    }

    /**
     * Atualiza os caracteres da música.
     * @param musica novos caracteres.
     */
    public void setMusica(List<String> musica) {
        this.musica = new ArrayList<String>(musica);
    }

    /**
     * Atualiza a duração da música.
     * @param duracao nova duração
     */
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    /**
     * Atualiza o número de reproduções.
     * @param numReproducoes número de reproduções.
     */
    public void setNumReproducoes(int numReproducoes) {
        this.numReproducoes = numReproducoes;
    }

    /**
     * Regista uma reprodução da música, incrementando o contador.
     */
    public void registaReproducao() {
        this.numReproducoes++;
    }

    /**
     * Devolve uma String contendo a letra da música.
     * @return letra da música.
     */
    public String imprimeLetra () {
        StringBuilder sb = new StringBuilder();
        for (String l: this.letra) {
            sb.append(l);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Implementa a reprodução de uma música individual, imprimindo a letra.
     * @param u utilizador que ouve a música
     * @return letra da música
     */
    public String reproduzMusica (Utilizador u) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString());
        sb.append("\n");
        for (String l: this.letra) {
            sb.append(l);
            sb.append("\n");
        }
        this.registaReproducao();
        return sb.toString();
    }

    /**
     * Diz se uma música é explícita ou não.
     * @return false
     */
    public boolean isExplicita () {
        return false;
    }

    /**
     * Diz se uma música é multimédia.
     * @return false
     */
    public boolean isMultimedia () {
        return false;
    }

    /**
     * Implementa igualdade entre músicas.
     * @param o objeto
     * @return true / false
     */
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Musica m = (Musica) o;
        return (this.nome.equals(m.nome)) && (this.interprete.equals(m.interprete)) && (this.genero.equals(m.genero));
    }

    /**
     * Representação em String de uma música.
     * @return nome - intérprete - género - editora
     */
    public String toString () {
        return this.nome + " - " + this.interprete + " - " + this.genero + " - " + this.editora;
    }

    /**
     * Clona uma música usando o construtor de cópia.
     * @return música clonada
     */
    public Musica clone () {
        return new Musica(this);
    }

    /**
     * Calcula o hash code de uma música.
     * @return hash code
     */
    public int hashCode () {
        return this.nome.hashCode() + this.interprete.hashCode() + this.genero.hashCode() + this.editora.hashCode() + this.letra.hashCode() + this.musica.hashCode() + this.duracao * 5 + this.numReproducoes * 3;
    }
}
