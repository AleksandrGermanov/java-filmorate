package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MpaaRating implements IdNamePair {
    private final Integer id;
    private final String name;
}
