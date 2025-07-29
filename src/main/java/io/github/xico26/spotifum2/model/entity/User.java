package io.github.xico26.spotifum2.model.entity;

import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.plan.IPlanoSubscricao;
import io.github.xico26.spotifum2.model.entity.plan.PlanoBase;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

/**
 * Implementa um utilizador.
 */

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="address")
    private String address;

    @Column(name="email")
    private String email;

    @Column(name="birth_date")
    private LocalDate birthDate;

    @Column(name="points")
    private int points;

    @Column(name="is_admin")
    private boolean isAdmin;

    @Column(name="wants_explicit")
    private boolean wantsExplicit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListeningRecord> listeningHistory = new ArrayList<>();

    private int age;
    private Biblioteca biblioteca;
    private IPlanoSubscricao plano;


    // Empty constructor
    public User() {
        this.name = "";
        this.username = "";
        this.password = "";
        this.address = "";
        this.email = "";
        this.birthDate = LocalDate.of(2000,1,1);
        updateAge();
        this.points = 0;
        this.isAdmin = false;
        this.wantsExplicit = false;
        this.listeningHistory = new ArrayList<ListeningRecord>();

        this.biblioteca = new Biblioteca();
        this.plano = new PlanoBase();
    }

    // Param constructor
    public User(String username, String password, String name, String address, String email, LocalDate birthDate) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.email = email;
        this.birthDate = birthDate;
        updateAge();
        this.points = 0;
        this.isAdmin = false;
        this.wantsExplicit = false;
        this.listeningHistory = new ArrayList<ListeningRecord>();

        this.biblioteca = new Biblioteca();
        this.plano = new PlanoBase();
    }

    // Copy constructor
    public User(User u) {
        this.name = u.getName();
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.address = u.getAddress();
        this.email = u.getEmail();
        this.birthDate = u.getBirthDate();
        updateAge();
        this.points = u.getPoints();
        this.isAdmin = u.isAdmin();
        this.wantsExplicit = u.wantsExplicit();
        this.listeningHistory = u.getListeningHistory();

        this.biblioteca = u.getBiblioteca();
        this.plano = u.getPlano();
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
    public String getName() {
        return name;
    }

    /**
     * Atualiza o nome.
     * @param nome novo nome
     */
    public void setName(String nome) {
        this.name = nome;
    }

    /**
     * Devolve a morada.
     * @return morada
     */
    public String getAddress() {
        return address;
    }

    /**
     * Atualiza a morada.
     * @param morada nova morada
     */
    public void setAddress(String morada) {
        this.address = morada;
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
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Atualiza a data de nascimento.
     * @param dataNascimento nova data de nascimento
     */
    public void setBirthDate(LocalDate dataNascimento) {
        this.birthDate = dataNascimento;
    }

    /**
     * Devolve a idade.
     * @return idade
     */
    public int getAge() {
        return age;
    }

    /**
     * Atualiza a idade.
     * @param idade nova idade
     */
    public void setAge(int idade) {
        this.age = idade;
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
    public void updateAge() {
        this.age = Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    /**
     * Devolve os pontos.
     * @return pontos
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Atualiza os pontos.
     * @param pontos novo valor dos pontos
     */
    public void setPoints(int pontos) {
        this.points = pontos;
    }

    /**
     * Adiciona pontos
     * @param pontos pontos
     */
    public void adicionarPontos(int pontos) {
        this.points += pontos;
    }

    public List<ListeningRecord> getListeningHistory() {
        return this.listeningHistory;
    }

    public void setMusicasOuvidas(List<ListeningRecord> listeningHistory) {
        this.listeningHistory = listeningHistory;
    }

    /**
     * Diz se o utilizador ouviu uma determinada música.
     * @param m música
     * @return true / false
     */
    public boolean ouviuMusica (Music m) {
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
    public void registaReproducaoMusica (Music m) {
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
    public boolean wantsExplicit() {
        return this.wantsExplicit;
    }

    public void setWantsExplicit(boolean wantsExplicit) {
        this.wantsExplicit = wantsExplicit;
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
        return this.username.hashCode() + this.password.hashCode() + this.name.hashCode() + this.address.hashCode() + this.email.hashCode() + this.birthDate.hashCode() * this.points;
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
        User u = (User) o;
        return (this.username.equals(u.getUsername())) && (this.password.equals(u.getPassword())) && (this.name.equals(u.getName())) && (this.address.equals(u.getAddress())) && (this.email.equals(u.getEmail())) && (this.birthDate.equals(u.getBirthDate())) && this.points == u.getPoints() && this.musicasOuvidas.equals(u.getListeningHistory()) && this.biblioteca.equals(u.getBiblioteca());
    }

    /**
     * Clona utilizador usando construtor de cópia
     * @return utilizador clonado
     */
    public User clone() {
        return new User(this);
    }

    /**
     * Representação em String de um utilizador
     * @return User: username
     */
    public String toString() {
        return "User: " + this.getUsername();
    }
}
