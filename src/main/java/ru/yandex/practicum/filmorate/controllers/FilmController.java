package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.validation.IsInSet;
import ru.yandex.practicum.filmorate.service.validation.Markers;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmController {
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private final FilmService service;

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

    @GetMapping("/popular")
    public List<Film> findMostLiked(@RequestParam(required = false) Integer count) {
        if (count == null || count <= 0) {
            return service.findMostLiked(10);
        } else {
            return service.findMostLiked(count);
        }
    }

    @GetMapping("/{id}")
    public Film retrieve(@IsInSet(setHolder = InMemoryFilmStorage.class) @PathVariable int id) {
        return storage.retrieve(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @IsInSet(setHolder = InMemoryFilmStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int userId) {
        service.addLike(userStorage.retrieve(userId),
                storage.retrieve(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(
            @IsInSet(setHolder = InMemoryFilmStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int userId) {
        service.removeLike(userStorage.retrieve(userId),
                storage.retrieve(id));
    }
}

