package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;

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

    public Set<Integer> getSet(){
        if (clas.equals(FilmController.class)){
            return FilmController.getFilms().keySet();
        }
        if(clas.equals(UserController.class)){
            return UserController.getUsers().keySet();
        }
        return Collections.emptySet();
    }
}