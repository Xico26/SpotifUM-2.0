package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

/**
 * Interface implementada por todos os planos. Contém todos os métodos que indicam as permissões dos utilizadores.
 */
public interface IPlanoSubscricao {

    boolean podeCriarPlaylist();

    boolean podeGuardarPlaylist();

    boolean podeGerarListaFavoritos();

    boolean podeGuardarAlbum();

    boolean podeAvancarRetroceder();

    boolean podeCriarListaGenero();

    boolean podeOuvirPlaylistConstruida();

    boolean podeOuvirMusicaIndividual();

    void adicionarPontos (Music music, User user);
}