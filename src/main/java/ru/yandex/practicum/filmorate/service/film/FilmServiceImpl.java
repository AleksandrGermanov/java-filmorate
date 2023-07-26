package ru.yandex.practicum.filmorate.service.film;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Validated
public class FilmServiceImpl implements FilmService {
    @Getter
    private final FilmStorage storage;
    private final UserService userService;

    @Override
    public void addLike(User whoLikes, Film film) {
        validateId(film);
        userService.validateId(whoLikes);
        Visitor<Film> likeGiver = (fi) -> fi.getLikes().add(whoLikes.getId());
        film.accept(likeGiver);
    }

    @Override
    public void removeLike(User whoTakesLikeBack, Film film) {
        validateId(film);
        userService.validateId(whoTakesLikeBack);
        Visitor<Film> likeTaker = (fi) -> fi.getLikes().remove(whoTakesLikeBack.getId());
        film.accept(likeTaker);
    }

    @Override
    public List<Film> findMostLiked(int count) {
        return storage.findAll().stream()
                .sorted(Collections.reverseOrder())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateId(int id) {
        boolean value = storage.findAll().stream()
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
        return storage.create(film);
    }

    @Override
    public Film retrieve(int id) {
        validateId(id);
        return storage.retrieve(id);
    }

    @Override
    public Film update(Film film) {
        validateId(film);
        return storage.update(film);
    }

    @Override
    public List<Film> findAll() {
        return storage.findAll();
    }
}

