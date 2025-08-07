package io.github.xico26.spotifum2.exceptions;

public class MusicNotFoundException extends RuntimeException {
    public MusicNotFoundException(String message) {
        super(message);
    }
}
