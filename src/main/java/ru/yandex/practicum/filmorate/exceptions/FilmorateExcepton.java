package ru.yandex.practicum.filmorate.exceptions;

public class FilmorateExcepton extends RuntimeException {

    public FilmorateExcepton() {
    }

    public FilmorateExcepton(String message) {
        super(message);
    }

    public FilmorateExcepton(String message, Throwable cause) {
        super(message, cause);
    }
}
