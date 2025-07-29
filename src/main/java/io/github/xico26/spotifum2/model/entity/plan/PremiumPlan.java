package io.github.xico26.spotifum2.model.entity.plan;

import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;

import java.io.Serializable;

/**
 * Plano de subscrição Premium Top. Atribui 2,5% dos pontos acumulados por cada nova música ouvida.
 */
public class PremiumPlan implements ISubscriptionPlan, Serializable {
    private static double bonusPercentual = 0.025;

    public boolean canCreatePlaylist() {
        return true;
    }

    public boolean canSavePlaylist() {
        return true;
    }

    public boolean podeGerarListaFavoritos() {
        return true;
    }

    public boolean canSaveAlbum() {
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

    public void addPoints(Music music, User user) {
        if (!user.ouviuMusica(music)) {
            int pontos = user.getPoints();
            int bonus = (int)(pontos * bonusPercentual);
            user.adicionarPontos(bonus);
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
