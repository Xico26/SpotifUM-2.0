package io.github.xico26.spotifum2.exceptions;

public class NameAlreadyUsedException extends RuntimeException {
    public NameAlreadyUsedException(String message) {
        super(message);
    }
}
