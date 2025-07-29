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


}
