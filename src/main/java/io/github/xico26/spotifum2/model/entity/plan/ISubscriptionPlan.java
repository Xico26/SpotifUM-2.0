package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

/**
 * Interface implementada por todos os planos. Contém todos os métodos que indicam as permissões dos utilizadores.
 */
public interface ISubscriptionPlan {

    boolean canCreatePlaylist();

    boolean canSavePlaylist();

    boolean canGenerateFavouritesList();

    boolean canSaveAlbum();

    boolean podeAvancarRetroceder();

    boolean canCreateGenreList();

    boolean canListenCustomPlaylist();

    boolean canListenSingleMusic();

    void addPoints(Music music, User user);
}