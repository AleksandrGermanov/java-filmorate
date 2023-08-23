package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaaRating;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmControllerTests {
    private final FilmController fc;
    Film film;

    @BeforeEach
    public void createFilm() {
        film = new Film();

        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2023, 7, 7));
        film.setDuration(1);
        film.setMpa(new MpaaRating(1, "Комедия"));
    }

    @Test
    void getAllFilmsReturnsArrayList() {
        assertEquals(ArrayList.class, fc.getAllFilms().getClass());
    }

    @Test
    void controllerSetsIdOnCreation() {
        assertNull(film.getId());
        assertNotNull(fc.createFilm(film).getId());
    }

    @Test
    void controllerPutsFilmToDBOnCreation() {
        Film f = fc.createFilm(film);
        assertTrue(fc.getAllFilms().contains(f));
    }

    @Test
    void controllerReturnsSameFilmAsInMapOnUpdate() {
        Film f = fc.createFilm(film);
        f.setName("NEW_NAME");
        assertEquals("NEW_NAME", fc.updateFilm(f).getName());
    }
}
