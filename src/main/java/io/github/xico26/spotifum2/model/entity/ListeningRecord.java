package io.github.xico26.spotifum2.model.entity;

import io.github.xico26.spotifum2.model.entity.music.Music;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_listened_music")
public class ListeningRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "music_id")
    private Music music;

    @Column(name = "listened_at", nullable = false)
    private LocalDateTime listenedAt;

    public ListeningRecord() {

    }

    public ListeningRecord(User user, Music music, LocalDateTime listenedAt) {
        this.user = user;
        this.music = music;
        this.listenedAt = listenedAt;
    }

    public ListeningRecord(ListeningRecord lr) {
        this.user = lr.getUser();
        this.music = lr.getMusic();
        this.listenedAt = lr.getListenedAt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public LocalDateTime getListenedAt() {
        return listenedAt;
    }

    public void setListenedAt(LocalDateTime listenedAt) {
        this.listenedAt = listenedAt;
    }

    @Override
    public int hashCode() {
        return id * 13 + user.hashCode() +  music.hashCode() + listenedAt.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ListeningRecord lr = (ListeningRecord) obj;
        return this.id == lr.getId();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ListeningRecord(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
