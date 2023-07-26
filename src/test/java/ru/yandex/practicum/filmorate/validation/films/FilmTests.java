package ru.yandex.practicum.filmorate.validation.films;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidatorForTests;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTests {
    private final ValidatorForTests<Film> filmValidator = new ValidatorForTests<>();
    private Film film;

    @BeforeEach
    public void createFilm() {
        film = new Film();

        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2023, 07, 07));
        film.setDuration(1);
    }

    @Test
    void validateEmptyFilm() {
        film = new Film();
        assertThrows(ConstraintViolationException.class, () -> filmValidator.isParameterValid(film));
    }

    @Test
    void validateDefaultFilm() {
        assertTrue(filmValidator.isParameterValid(film));
    }

    @Test
    void validateFilmWithBlankName() {
        film.setName(" \n");
        assertThrows(ConstraintViolationException.class, () -> filmValidator.isParameterValid(film));
    }

    @Test
    void validateFilmWithOversizeDescription() {
        String twoHundredOneCharString = ".".repeat(201);
        film.setDescription(twoHundredOneCharString);
        assertThrows(ConstraintViolationException.class, () -> filmValidator.isParameterValid(film));
    }

    @Test
    void validateFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1010, 10, 10));
        assertThrows(ConstraintViolationException.class, () -> filmValidator.isParameterValid(film));
    }

    @Test
    void validateFilmWithZeroDuration() {
        film.setDuration(0);
        assertThrows(ConstraintViolationException.class, () -> filmValidator.isParameterValid(film));
    }
}