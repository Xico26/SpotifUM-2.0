package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.io.Serializable;

/**
 * Plano de subscrição Premium Base. Atribui 10 pontos por música.
 */
public class PlusPlan implements ISubscriptionPlan, Serializable {
    private static int pontosPorMusica = 10;


    public boolean canCreatePlaylist() {
        return true;
    }

    public boolean canSavePlaylist() {
        return true;
    }

    public boolean canSaveAlbum() {
        return true;
    }

    public boolean podeAvancarRetroceder() {
        return true;
    }

    public boolean podeGerarListaFavoritos() {
        return false;
    }

    public boolean podeCriarListaGenero() {
        return false;
    }

    public boolean podeOuvirMusicaIndividual() {
        return true;
    }

    public void addPoints(Music music, User user) {
        user.adicionarPontos(pontosPorMusica);
    }

    public boolean podeOuvirPlaylistConstruida() {
        return true;
    }

    public static int getPontosPorMusica() {
        return pontosPorMusica;
    }

    public static void setPontosPorMusica(int pontos) {
        pontosPorMusica = pontos;
    }

    public String toString() {
        return "Premium Base";
    }
}