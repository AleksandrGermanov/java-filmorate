package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.Set;

public class IdValidator implements ConstraintValidator<IsInSet, Integer> {
    private Class<?> clas;

    @Override
    public void initialize(IsInSet isInSet) {
        if (isInSet.setHolder() != null) {
            clas = isInSet.setHolder();
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return getSet().contains(integer);
    }

    public Set<Integer> getSet() {
        if (clas.equals(InMemoryFilmStorage.class)) {
            return InMemoryFilmStorage.getFilms().keySet();
        }
        if (clas.equals(InMemoryUserStorage.class)) {
            return InMemoryUserStorage.getUsers().keySet();
        }
        return Collections.emptySet();
    }
}