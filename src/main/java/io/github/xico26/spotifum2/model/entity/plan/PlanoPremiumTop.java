package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.Utilizador;
import io.github.xico26.spotifum2.model.entity.music.Musica;

import java.io.Serializable;

/**
 * Plano de subscrição Premium Top. Atribui 2,5% dos pontos acumulados por cada nova música ouvida.
 */
public class PlanoPremiumTop implements IPlanoSubscricao, Serializable {
    private static double bonusPercentual = 0.025;

    public boolean podeCriarPlaylist() {
        return true;
    }

    public boolean podeGuardarPlaylist() {
        return true;
    }

    public boolean podeGerarListaFavoritos() {
        return true;
    }

    public boolean podeGuardarAlbum() {
        return true;
    }

    public boolean podeAvancarRetroceder() {
        return true;
    }

    public boolean podeCriarListaGenero() {
        return true;
    }

    public boolean podeOuvirPlaylistConstruida() {
        return true;
    }

    public boolean podeOuvirMusicaIndividual() {
        return true;
    }

    public void adicionarPontos(Musica musica, Utilizador utilizador) {
        if (!utilizador.ouviuMusica(musica)) {
            int pontos = utilizador.getPontos();
            int bonus = (int)(pontos * bonusPercentual);
            utilizador.adicionarPontos(bonus);
        }
    }

    public static double getBonusPercentual() {
        return bonusPercentual;
    }

    public static void setBonusPercentual(double bonus) {
        bonusPercentual = bonus;
    }

    public String toString() {
        return "Premium Top";
    }
}
