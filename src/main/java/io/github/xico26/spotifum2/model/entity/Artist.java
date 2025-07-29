package io.github.xico26.spotifum2.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="country")
    private String country;

    @Column(name="birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name="death_date")
    private LocalDate deathDate;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums = new ArrayList<>();

    public Artist() {
        this.name = "";
        this.country = "";
        this.birthDate = LocalDate.of(1900, 1, 1);
        this.deathDate = null;
        this.albums = new ArrayList<>();
    }

    public Artist(Artist a) {
        this.name = a.getName();
        this.country = a.getCountry();
        this.birthDate = a.getBirthDate();
        this.deathDate = a.getDeathDate();
        this.albums = a.getAlbums();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public List<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums.addAll(albums);
    }

    @Override
    public int hashCode() {
        return this.id * 17 + this.name.hashCode() +  this.country.hashCode() + this.birthDate.hashCode() + (this.deathDate == null ? 0 : this.deathDate.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Artist a =  (Artist) obj;
        return this.id == a.id && this.name.equals(a.name) && this.country.equals(a.country);
    }

    @Override
    public Artist clone() {
        return new Artist(this);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
