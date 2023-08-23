package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaaRating;
import ru.yandex.practicum.filmorate.service.idNamePair.GenreService;
import ru.yandex.practicum.filmorate.service.idNamePair.MpaaRatingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IdNamePairController {
    private final MpaaRatingService mpaaService;
    private final GenreService genreService;

    @GetMapping("/mpa")
    public List<MpaaRating> findAllRatings() {
        log.info("Endpoint /mpa} invoked (GET)");
        return mpaaService.findAll();
    }

    @GetMapping("/mpa/{id}")
    public MpaaRating retrieveRating(@PathVariable int id) {
        log.info("Endpoint /mpa/{id}} invoked (GET)");
        return mpaaService.retrieve(id);
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        log.info("Endpoint /genres} invoked (GET)");
        return genreService.findAll();
    }

    @GetMapping("/genres/{id}")
    public Genre retrieveGenre(@PathVariable int id) {
        log.info("Endpoint /genres/{id}} invoked (GET)");
        return genreService.retrieve(id);
    }
}
