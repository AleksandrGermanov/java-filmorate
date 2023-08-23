package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final UserService userService;
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Endpoint /films invoked (GET)");
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Endpoint /films invoked (POST)");
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Endpoint /films invoked (PUT)");
        return filmService.update(film);
    }

    @GetMapping("/popular")
    public List<Film> findMostLiked(@RequestParam(defaultValue = "10")
                                    @Positive @Valid Integer count) {
        log.info("Endpoint /films/popular invoked (GET)");
        return filmService.findMostLiked(count);
    }

    @GetMapping("/{id}")
    public Film retrieve(@PathVariable int id) {
        log.info("Endpoint /films/{id}} invoked (GET)");
        return filmService.retrieve(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable int id,
            @PathVariable int userId) {
        log.info("Endpoint /films/{id}/like/{userId}} invoked (PUT)");
        filmService.addLike(userService.retrieve(userId),
                filmService.retrieve(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(
            @PathVariable int id,
            @PathVariable int userId) {
        log.info("Endpoint /films/{id}/like/{userId}} invoked (DELETE)");
        filmService.removeLike(userService.retrieve(userId),
                filmService.retrieve(id));
    }
}