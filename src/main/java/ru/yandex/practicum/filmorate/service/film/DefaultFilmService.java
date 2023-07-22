package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmVisitor;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class DefaultFilmService implements FilmService {
    @Override
    public void addLike(User whoLikes, Film film) {
        FilmVisitor likeGiver = (fi) -> fi.getLikes().add(whoLikes.getId());
        film.accept(likeGiver);
    }

    @Override
    public void removeLike(User whoTakesLikeBack, Film film) {
        FilmVisitor likeTaker = (fi) -> fi.getLikes().add(whoTakesLikeBack.getId());
        film.accept(likeTaker);
    }

    @Override
    public List<Film> findMostLiked(int count) {
        List<Film> mostLikedOnTop = InMemoryFilmStorage.getFilms().values().stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        List<Film> result = new ArrayList<>();
        for (Film film : mostLikedOnTop) {
            result.add(film);
            if (--count == 0) {
                break;
            }
        }
        return result;
    }
}
