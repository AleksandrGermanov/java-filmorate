package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {
    Film create(Film film);

    Film retrieve(int id);

    Film update(Film film);

    List<Film> findAll();
}
