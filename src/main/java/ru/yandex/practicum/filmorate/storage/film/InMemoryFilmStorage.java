package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Getter
    private static final Map<Integer, Film> films = new HashMap<>();
    private static int idCounter; //пока нет более внятного присвоения id

    @Override
    public Film create(Film film) {
        film.setId(generateId());
        log.info("POST /films is successfully proceed. Film id = " + film.getId() + ".");
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film retrieve(int id) {
        log.info("GET /films/{id} is successfully proceed. Film id = " + id + ".");
        return films.get(id);
    }

    @Override
    public Film update(Film film) {
        log.info("PUT /films is successfully proceed. Film id = " + film.getId() + ".");
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        log.info("GET /films is successfully proceed.");
        return new ArrayList<>(films.values());
    }

    private int generateId() {
        return ++idCounter;
    } //Вынес в отдельный метод, т.к. это побочный эффект при создании
    //фильма.
}
