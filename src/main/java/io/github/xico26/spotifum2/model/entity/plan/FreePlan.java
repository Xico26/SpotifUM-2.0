package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.io.Serializable;

/**
 * Plano de subscrição Base. Atribui 5 pontos por música.
 */
public class FreePlan implements ISubscriptionPlan, Serializable {
    private static int pontosPorMusica = 5;


    public boolean canCreatePlaylist() {
        return false;
    }

    public boolean canSavePlaylist() {
        return false;
    }

    public boolean canGenerateFavouritesList() {
        return false;
    }

    public boolean canSaveAlbum() {
        return false;
    }

    public boolean podeAvancarRetroceder() {
        return false;
    }

    public boolean canCreateGenreList() {
        return false;
    }

    public boolean canListenCustomPlaylist() {
        return false;
    }

    public boolean canListenSingleMusic() {
        return false;
    }

    public void addPoints(Music music, User user) {
        user.addPoints(pontosPorMusica);
    }

    public static int getPontosPorMusica() {
        return pontosPorMusica;
    }

    public static void setPontosPorMusica(int pontos) {
        pontosPorMusica = pontos;
    }

    public String toString() {
        return "Base";
    }
}

