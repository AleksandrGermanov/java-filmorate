package ru.yandex.practicum.filmorate.service.idNamePair;

import ru.yandex.practicum.filmorate.model.IdNamePair;

import java.util.List;

public interface IdNamePairService<T extends IdNamePair> {
    List<T> findAll();

    T retrieve(int id);
}
