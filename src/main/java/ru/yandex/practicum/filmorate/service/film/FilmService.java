package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public interface FilmService {
    void addLike(User whoLikes, Film film);

    void removeLike(User whoTakesLikeBack, Film film);

    List<Film> findMostLiked(int count);

    boolean validateId(int id);

    boolean validateId(Film film);

    Film create(Film film);

    Film retrieve(int id);

    Film update(Film film);

    List<Film> findAll();
}
