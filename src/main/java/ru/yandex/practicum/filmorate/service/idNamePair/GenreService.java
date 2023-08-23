package ru.yandex.practicum.filmorate.service.idNamePair;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MatchesNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.idNamePair.GenreDBStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class GenreService implements IdNamePairService<Genre> {
    private final GenreDBStorage genreDBStorage;

    @Override
    public List<Genre> findAll() {
        log.info("Proceeding findAll().");
        return genreDBStorage.findAll();
    }

    @Override
    public Genre retrieve(int id) {
        log.info("Proceeding retrieve() with id = " + id + ".");
        validateId(id);
        return genreDBStorage.retrieve(id);
    }

    public boolean validateId(int id) {
        boolean value = genreDBStorage.findAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet())
                .contains(id);
        if (!value) {
            throw new MatchesNotFoundException("Жанр с id " + id + " не найден.");
        }
        return true;
    }
}
