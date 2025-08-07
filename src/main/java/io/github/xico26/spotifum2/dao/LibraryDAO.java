package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Library;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.util.List;

public interface LibraryDAO {
    Library findByUser (User u);

    void save (Library library);

    void update (Library library);

    List<Library> findAllWithMusic(Music music);
}
