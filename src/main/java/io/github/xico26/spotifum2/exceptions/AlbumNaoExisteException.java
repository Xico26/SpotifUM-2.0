package io.github.xico26.spotifum2.exceptions;

public class AlbumNaoExisteException extends RuntimeException {
    public AlbumNaoExisteException(String message) {
        super(message);
    }
}
