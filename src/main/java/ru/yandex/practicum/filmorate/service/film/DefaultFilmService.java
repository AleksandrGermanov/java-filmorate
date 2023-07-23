package ru.yandex.practicum.filmorate.service.film;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultFilmService implements FilmService {
    @Getter
    private final FilmStorage storage;

    @Override
    public void addLike(User whoLikes, Film film) {
        Visitor<Film> likeGiver = (fi) -> fi.getLikes().add(whoLikes.getId());
        film.accept(likeGiver);
    }

    @Override
    public void removeLike(User whoTakesLikeBack, Film film) {
        Visitor<Film> likeTaker = (fi) -> fi.getLikes().remove(whoTakesLikeBack.getId());
        film.accept(likeTaker);
    }

    @Override
    public List<Film> findMostLiked(int count) {
        return storage.findAll().stream()
                .sorted(Collections.reverseOrder()).limit(count)
                .collect(Collectors.toList());
    }
}

