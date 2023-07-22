package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.Markers;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    @Getter
    private FilmStorage storage;

    @Autowired
    public void setFilmStorage(FilmStorage storage) {
        this.storage = storage;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return storage.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return storage.create(film);
    }

    @PutMapping
    @Validated(Markers.OnUpdate.class)
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return storage.update(film);
    }


}
