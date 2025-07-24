package io.github.xico26;

public class PlaylistNaoExisteException extends RuntimeException {
    public PlaylistNaoExisteException(String message) {
        super(message);
    }
}
