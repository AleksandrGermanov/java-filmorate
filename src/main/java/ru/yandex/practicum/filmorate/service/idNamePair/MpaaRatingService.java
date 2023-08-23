package ru.yandex.practicum.filmorate.service.idNamePair;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MatchesNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaaRating;
import ru.yandex.practicum.filmorate.storage.idNamePair.MpaaRatingDBStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class MpaaRatingService implements IdNamePairService<MpaaRating> {
    private final MpaaRatingDBStorage storage;

    @Override
    public List<MpaaRating> findAll() {
        log.info("Proceeding findAll().");
        return storage.findAll();
    }

    @Override
    public MpaaRating retrieve(int id) {
        log.info("Proceeding retrieve() with id = " + id + ".");
        validateId(id);
        return storage.retrieve(id);
    }

    private boolean validateId(int id) {
        boolean value = storage.findAll().stream()
                .map(MpaaRating::getId)
                .collect(Collectors.toSet())
                .contains(id);
        if (!value) {
            throw new MatchesNotFoundException("Рейтинг с id " + id + " не найден.");
        }
        return true;
    }
}
