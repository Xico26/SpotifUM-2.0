package io.github.xico26.spotifum2.exceptions;

public class PlaylistAlreadySavedException extends RuntimeException {
    public PlaylistAlreadySavedException(String message) {
        super(message);
    }
}
