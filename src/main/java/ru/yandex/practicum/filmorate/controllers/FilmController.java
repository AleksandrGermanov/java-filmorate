package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Markers;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    @Getter
    private static final  Map<Integer, Film> films = new HashMap<>();
    private static int idCounter; //пока нет более внятного присвоения id

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET /films is successfully proceed.");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(++idCounter);
        log.info("POST /films is successfully proceed. Film id = " + film.getId() + ".");
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    @Validated(Markers.OnUpdate.class)
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT /films is successfully proceed. Film id =" + film.getId() + ".");
        films.put(film.getId(), film);
        return film;
    }
}
