package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.DefaultFilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FilmControllerTests {
    FilmController fc = new FilmController(new InMemoryFilmStorage(),
            new InMemoryUserStorage(), new DefaultFilmService());
    Film film;

    @BeforeEach
    public void createFilm() {
        film = new Film();

        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2023, 7, 7));
        film.setDuration(1);
    }

    @Test
    void getAllFilmsReturnsArrayList() {
        assertEquals(ArrayList.class, fc.getAllFilms().getClass());
    }

    @Test
    void controllerSetsIdOnCreation() {
        int oldId = film.getId();

        fc.createFilm(film);
        assertNotEquals(oldId, film.getId());
    }

    @Test
    void controllerPutsFilmToMapOnCreation() {
        fc.createFilm(film);
        assertEquals(film, InMemoryFilmStorage.getFilms().get(film.getId()));
    }

    @Test
    void controllerReturnsSameFilmAsInMapOnCreation() {
        assertEquals(fc.createFilm(film), InMemoryFilmStorage.getFilms().get(film.getId()));
    }

    @Test
    void controllerReturnsSameFilmAsInMapOnUpdate() {
        assertEquals(fc.updateFilm(film), InMemoryFilmStorage.getFilms().get(film.getId()));
    }
}
