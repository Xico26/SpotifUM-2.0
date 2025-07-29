package io.github.xico26.spotifum2.exceptions;

public class PlaylistNaoExisteException extends RuntimeException {
    public PlaylistNaoExisteException(String message) {
        super(message);
    }
}
