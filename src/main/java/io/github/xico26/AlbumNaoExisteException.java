package io.github.xico26;

public class AlbumNaoExisteException extends RuntimeException {
    public AlbumNaoExisteException(String message) {
        super(message);
    }
}
