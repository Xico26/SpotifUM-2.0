package io.github.xico26.spotifum2.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
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

    @Column(name="subscription_plan")
    private String subscriptionPlan;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListeningRecord> listeningHistory = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Library library;

    private int age;


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
        this.subscriptionPlan = "FREE";
        this.listeningHistory = new ArrayList<ListeningRecord>();

        this.library = new Library();
    }

    // Param constructor
    public User(String username, String password, String name, String address, String email, LocalDate birthDate, String plan) {
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
        this.subscriptionPlan = plan;
        this.listeningHistory = new ArrayList<ListeningRecord>();
        this.library = new Library(this);
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
        this.subscriptionPlan = u.getSubscriptionPlan();
        this.listeningHistory = u.getListeningHistory();
        this.library = new Library(u.getLibrary());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isWantsExplicit() {
        return wantsExplicit;
    }

    public void setListeningHistory(List<ListeningRecord> listeningHistory) {
        this.listeningHistory = listeningHistory;
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

    public String getSubscriptionPlan() {
        return this.subscriptionPlan;
    }

    public void setSubscriptionPlan(String newPlan) {
        this.subscriptionPlan = newPlan;
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
     * @param points pontos
     */
    public void addPoints(int points) {
        this.points += points;
    }

    public List<ListeningRecord> getListeningHistory() {
        return this.listeningHistory;
    }

    public void setMusicasOuvidas(List<ListeningRecord> listeningHistory) {
        this.listeningHistory = listeningHistory;
    }

    /**
     * Devolve a library do utilizador.
     * @return library
     */
    public Library getLibrary() {
        return this.library;
    }

    /**
     * Atualiza a library do utilizador.
     * @param b nova library
     */
    public void setLibrary(Library b) {
        this.library = new Library(b);
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
        return (this.username.equals(u.getUsername())) && (this.password.equals(u.getPassword())) && (this.name.equals(u.getName())) && (this.address.equals(u.getAddress())) && (this.email.equals(u.getEmail())) && (this.birthDate.equals(u.getBirthDate())) && this.points == u.getPoints() && this.library.equals(u.getLibrary());
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
