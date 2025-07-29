package io.github.xico26.spotifum2.exceptions;

public class MusicaNaoExisteException extends RuntimeException {
    public MusicaNaoExisteException(String message) {
        super(message);
    }
}
