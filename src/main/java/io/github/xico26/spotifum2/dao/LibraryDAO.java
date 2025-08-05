package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Library;
import io.github.xico26.spotifum2.model.entity.User;

public interface LibraryDAO {
    Library findByUser (User u);

    void save (Library library);

    void update (Library library);
}
