package io.github.xico26;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementa um utilizador.
 */
public class Utilizador implements Serializable {
    private String username;
    private String password;
    private String nome;
    private String morada;
    private String email;
    private LocalDate dataNascimento;
    private int idade;
    private int pontos;
    private Map<Musica,List<LocalDateTime>> musicasOuvidas; // musica - datas ouvidas
    private Biblioteca biblioteca;
    private boolean isAdmin;
    private IPlanoSubscricao plano;
    private boolean querVerExplicita;
    private boolean querVerMultimedia;

    /**
     * Construtor por omissão.
     */
    public Utilizador () {
        this.username = "";
        this.password = "";
        this.nome = "";
        this.morada = "";
        this.email = "";
        this.dataNascimento = LocalDate.of(2000,1,1);
        atualizaIdade();
        this.pontos = 0;
        this.musicasOuvidas = new HashMap<Musica,List<LocalDateTime>>();
        this.biblioteca = new Biblioteca();
        this.isAdmin = false;
        this.plano = new PlanoBase();
        this.querVerExplicita = false;
        this.querVerMultimedia = false;
    }

    /**
     * Construtor parametrizado. Aceita:
     * @param username username
     * @param password password
     * @param nome nome
     * @param morada morada
     * @param email email
     * @param dataNascimento data de nascimento
     */
    public Utilizador(String username, String password, String nome, String morada, String email, LocalDate dataNascimento) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.morada = morada;
        this.email = email;
        this.dataNascimento = dataNascimento;
        atualizaIdade();
        this.pontos = 0;
        this.musicasOuvidas = new HashMap<Musica,List<LocalDateTime>>();
        this.biblioteca = new Biblioteca();
        this.isAdmin = false;
        this.plano = new PlanoBase();
        this.querVerExplicita = false;
        this.querVerMultimedia = false;
    }

    /**
     * Construtor de cópia. Aceita:
     * @param u utilizador a copiar
     */
    public Utilizador (Utilizador u) {
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.nome = u.getNome();
        this.morada = u.getMorada();
        this.email = u.getEmail();
        this.dataNascimento = u.getDataNascimento();
        atualizaIdade();
        this.pontos = u.getPontos();
        this.musicasOuvidas = u.getMusicasOuvidas();
        this.biblioteca = u.getBiblioteca();
        this.isAdmin = u.isAdmin();
        this.plano = u.getPlano();
        this.querVerExplicita = u.querVerExplicita();
        this.querVerMultimedia = u.querVerMultimedia();
    }

    /**
     * Devolve o nome de utilizador.
     * @return nome de utilizador
     */
    public String getUsername() {
        return username;
    }

    /**
     * Atualiza o nome de utilizador.
     * @param username novo nome de utilizador
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devolve a password.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Atualiza a password.
     * @param password nova password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Devolve o nome.
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Atualiza o nome.
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Devolve a morada.
     * @return morada
     */
    public String getMorada() {
        return morada;
    }

    /**
     * Atualiza a morada.
     * @param morada nova morada
     */
    public void setMorada(String morada) {
        this.morada = morada;
    }

    /**
     * Devolve o email.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Atualiza o email.
     * @param email novo email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devolve a data de nascimento.
     * @return data de nascimento
     */
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    /**
     * Atualiza a data de nascimento.
     * @param dataNascimento nova data de nascimento
     */
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * Devolve a idade.
     * @return idade
     */
    public int getIdade() {
        return idade;
    }

    /**
     * Atualiza a idade.
     * @param idade nova idade
     */
    public void setIdade(int idade) {
        this.idade = idade;
    }

    /**
     * Diz se o utilizador é administrador.
     * @return true / false
     */
    public boolean isAdmin() {
        return this.isAdmin;
    }

    /**
     * Atualiza o estado de administrador.
     * @param isAdmin true se for administrador
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Devolve o plano de subscrição.
     * @return plano de subscrição
     */
    public IPlanoSubscricao getPlano() {
        return this.plano;
    }

    /**
     * Atualiza o plano de subscrição.
     * @param plano novo plano de subscrição
     */
    public void setPlano(IPlanoSubscricao plano) {
        this.plano = plano;
    }

    /**
     * Atualiza automaticamente a idade com base na data de nascimento e na data atual.
     */
    public void atualizaIdade() {
        this.idade = Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    /**
     * Devolve os pontos.
     * @return pontos
     */
    public int getPontos() {
        return this.pontos;
    }

    /**
     * Atualiza os pontos.
     * @param pontos novo valor dos pontos
     */
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    /**
     * Adiciona pontos
     * @param pontos pontos
     */
    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }

    /**
     * Devolve as músicas ouvidas.
     * @return músicas ouvidas
     */
    public Map<Musica,List<LocalDateTime>> getMusicasOuvidas() {
        Map<Musica,List<LocalDateTime>> musicasOuvidasClone = new HashMap<Musica,List<LocalDateTime>>();
        for (Map.Entry<Musica,List<LocalDateTime>> m : this.musicasOuvidas.entrySet()) {
            List<LocalDateTime> datasClone = new ArrayList<LocalDateTime>(m.getValue());
            musicasOuvidasClone.put(m.getKey().clone(), datasClone);
        }
        return musicasOuvidasClone;
    }

    /**
     * Atualiza as músicas ouvidas.
     * @param ms novas músicas
     */
    public void setMusicasOuvidas(Map<Musica, List<LocalDateTime>> ms) {
        this.musicasOuvidas = new HashMap<Musica,List<LocalDateTime>>();

        for (Map.Entry<Musica, List<LocalDateTime>> m : ms.entrySet()) {
            Musica musicaClone = m.getKey().clone();
            List<LocalDateTime> datasClone = new ArrayList<>(m.getValue());

            this.musicasOuvidas.put(musicaClone, datasClone);
        }
    }

    /**
     * Diz se o utilizador ouviu uma determinada música.
     * @param m música
     * @return true / false
     */
    public boolean ouviuMusica (Musica m) {
        return this.musicasOuvidas.containsKey(m);
    }

    /**
     * Devolve o número de músicas ouvidas.
     * @return número de músicas ouvidas
     */
    public int getNumMusicasOuvidas() {
        return this.musicasOuvidas.size();
    }

    /**
     * Regista a reprodução de uma música.
     * Caso seja a primeira vez, adiciona a música ao histórico.
     * Também adiciona pontos ao utilizador com base no plano de subscrição.
     * @param m música reproduzida
     */
    public void registaReproducaoMusica (Musica m) {
        this.getPlano().adicionarPontos(m, this);
        LocalDateTime agora = LocalDateTime.now();
        if (this.musicasOuvidas.containsKey(m)) {
            this.musicasOuvidas.get(m).add(agora);
        } else {
            List<LocalDateTime> datas = new ArrayList<>();
            datas.add(agora);
            this.musicasOuvidas.put(m, datas);
        }
    }

    /**
     * Devolve a biblioteca do utilizador.
     * @return biblioteca
     */
    public Biblioteca getBiblioteca() {
        return this.biblioteca;
    }

    /**
     * Atualiza a biblioteca do utilizador.
     * @param b nova biblioteca
     */
    public void setBiblioteca (Biblioteca b) {
        this.biblioteca = new Biblioteca(b);
    }

    /**
     * Diz se o utilizador quer ver músicas explícitas.
     * @return true / false
     */
    public boolean querVerExplicita () {
        return this.querVerExplicita;
    }

    /**
     * Atualiza se o utilizador quer ver músicas explícitas.
     * @param querVerExplicita true / false
     */
    public void setQuerVerExplicita (boolean querVerExplicita) {
        this.querVerExplicita = querVerExplicita;
    }

    /**
     * Diz se o utilizador quer ver músicas multimédia.
     * @return true / false
     */
    public boolean querVerMultimedia () {
        return this.querVerMultimedia;
    }

    /**
     * Atualiza se o utilizador quer ver músicas multimédia.
     * @param querVerMultimedia true / false
     */
    public void setQuerVerMultimedia (boolean querVerMultimedia) {
        this.querVerMultimedia = querVerMultimedia;
    }

    /**
     * Apaga o histórico de músicas ouvidas.
     */
    public void apagaHistorico() {
        this.musicasOuvidas.clear();
    }

    /**
     * Calcula o hash code de um utilizador.
     * @return hash code
     */
    public int hashCode() {
        return this.username.hashCode() + this.password.hashCode() + this.nome.hashCode() + this.morada.hashCode() + this.email.hashCode() + this.dataNascimento.hashCode() * this.pontos;
    }

    /**
     * Implementa igualdade entre utilizadores
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
        Utilizador u = (Utilizador) o;
        return (this.username.equals(u.getUsername())) && (this.password.equals(u.getPassword())) && (this.nome.equals(u.getNome())) && (this.morada.equals(u.getMorada())) && (this.email.equals(u.getEmail())) && (this.dataNascimento.equals(u.getDataNascimento())) && this.pontos == u.getPontos() && this.musicasOuvidas.equals(u.getMusicasOuvidas()) && this.biblioteca.equals(u.getBiblioteca());
    }

    /**
     * Clona utilizador usando construtor de cópia
     * @return utilizador clonado
     */
    public Utilizador clone() {
        return new Utilizador(this);
    }

    /**
     * Representação em String de um utilizador
     * @return Utilizador: username
     */
    public String toString() {
        return "Utilizador: " + this.getUsername();
    }
}
