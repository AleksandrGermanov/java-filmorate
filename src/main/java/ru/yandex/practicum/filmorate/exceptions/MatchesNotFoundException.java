package ru.yandex.practicum.filmorate.exceptions;

public class MatchesNotFoundException extends FilmorateExcepton {
    public MatchesNotFoundException() {
    }

    public MatchesNotFoundException(String message) {
        super(message);
    }

    public MatchesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
