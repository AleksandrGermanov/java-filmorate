package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorForTests<V> {

    private final Validator validator;

    public ValidatorForTests() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public boolean isParameterValid(V v) {
        Set<ConstraintViolation<V>> violations = validator.validate(v);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return true;
    }
}
