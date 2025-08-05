package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.ListeningRecord;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.util.List;

public interface ListeningRecordDAO {
    List<ListeningRecord> findByUser(User u);

    void deleteByUser(User u);

    void save(ListeningRecord listeningRecord);

    boolean hasListened (User u, int musicId);

    int getNumListened (User u);
}
