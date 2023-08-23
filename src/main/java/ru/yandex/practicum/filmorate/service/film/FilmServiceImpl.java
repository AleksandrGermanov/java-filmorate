package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.MatchesNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Override
    public void addLike(User whoLikes, Film film) {
        log.info("Proceeding addLike() with user = " + whoLikes.getId() + " and film " + film.getId() + ".");
        validateId(film);
        userService.validateId(whoLikes);
        Visitor<Film> likeGiver = (fi) -> {
            fi.getLikes().add(whoLikes.getId());
            filmStorage.update(fi);
        };
        film.accept(likeGiver);
    }

    @Override
    public void removeLike(User whoTakesLikeBack, Film film) {
        log.info("Proceeding removeLike() with user = " + whoTakesLikeBack.getId() + " and film " + film.getId() + ".");
        validateId(film);
        userService.validateId(whoTakesLikeBack);
        Visitor<Film> likeTaker = (fi) -> {
            fi.getLikes().remove(whoTakesLikeBack.getId());
            filmStorage.update(fi);
        };
        film.accept(likeTaker);
    }

    @Override
    public List<Film> findMostLiked(int count) {
        log.info("Proceeding findMostLiked() with count = " + count + ".");
        return filmStorage.findAll().stream()
                .sorted(Collections.reverseOrder())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateId(int id) {
        boolean value = filmStorage.findAll().stream()
                .map(Film::getId)
                .collect(Collectors.toSet())
                .contains(id);
        if (!value) {
            throw new MatchesNotFoundException("Фильм с id " + id + " не найден.");
        }
        return true;
    }

    @Override
    public boolean validateId(Film film) {
        return validateId(film.getId());
    }

    @Override
    public Film create(Film film) {
        log.info("Proceeding findMostLiked() with film = " + film.getId() + ".");
        return filmStorage.create(film);
    }

    @Override
    public Film retrieve(int id) {
        log.info("Proceeding retrieve() with film = " + id + ".");
        validateId(id);
        return filmStorage.retrieve(id);
    }

    @Override
    public Film update(Film film) {
        log.info("Proceeding retrieve() with film = " + film.getId() + ".");
        validateId(film);
        return filmStorage.update(film);
    }

    @Override
    public List<Film> findAll() {
        log.info("Proceeding findAll().");
        return filmStorage.findAll();
    }
}

