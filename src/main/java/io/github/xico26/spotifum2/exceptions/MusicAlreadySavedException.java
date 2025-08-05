package io.github.xico26.spotifum2.exceptions;

public class MusicAlreadySavedException extends RuntimeException {
    public MusicAlreadySavedException(String message) {
        super(message);
    }
}
