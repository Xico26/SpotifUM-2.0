package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.Utilizador;
import io.github.xico26.spotifum2.model.entity.music.Musica;

import java.io.Serializable;

/**
 * Plano de subscrição Base. Atribui 5 pontos por música.
 */
public class PlanoBase implements IPlanoSubscricao, Serializable {
    private static int pontosPorMusica = 5;


    public boolean podeCriarPlaylist() {
        return false;
    }

    public boolean podeGuardarPlaylist() {
        return false;
    }

    public boolean podeGerarListaFavoritos() {
        return false;
    }

    public boolean podeGuardarAlbum() {
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

    public void adicionarPontos(Musica musica, Utilizador utilizador) {
        utilizador.adicionarPontos(pontosPorMusica);
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

