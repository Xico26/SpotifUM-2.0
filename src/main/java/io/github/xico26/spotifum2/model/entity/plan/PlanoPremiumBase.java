package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.Utilizador;
import io.github.xico26.spotifum2.model.entity.music.Musica;

import java.io.Serializable;

/**
 * Plano de subscrição Premium Base. Atribui 10 pontos por música.
 */
public class PlanoPremiumBase implements IPlanoSubscricao, Serializable {
    private static int pontosPorMusica = 10;


    public boolean podeCriarPlaylist() {
        return true;
    }

    public boolean podeGuardarPlaylist() {
        return true;
    }

    public boolean podeGuardarAlbum() {
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

    public void adicionarPontos(Musica musica, Utilizador utilizador) {
        utilizador.adicionarPontos(pontosPorMusica);
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