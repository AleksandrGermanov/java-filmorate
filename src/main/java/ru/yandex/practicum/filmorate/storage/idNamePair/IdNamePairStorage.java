package ru.yandex.practicum.filmorate.storage.idNamePair;

import ru.yandex.practicum.filmorate.model.IdNamePair;

import java.util.List;

public interface IdNamePairStorage<T extends IdNamePair> {
    List<T> findAll();

    T retrieve(int id);
}
