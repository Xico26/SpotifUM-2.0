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

    public boolean podeGerarListaFavoritos() {
        return false;
    }

    public boolean canSaveAlbum() {
        return false;
    }

    public boolean podeAvancarRetroceder() {
        return false;
    }

    public boolean podeCriarListaGenero() {
        return false;
    }

    public boolean podeOuvirPlaylistConstruida() {
        return false;
    }

    public boolean podeOuvirMusicaIndividual() {
        return false;
    }

    public void addPoints(Music music, User user) {
        user.adicionarPontos(pontosPorMusica);
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

