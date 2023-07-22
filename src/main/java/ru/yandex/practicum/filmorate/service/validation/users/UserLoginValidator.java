package ru.yandex.practicum.filmorate.service.validation.users;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class UserLoginValidator implements ConstraintValidator<NoSpaceSigns, String> {
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login != null && !login.isBlank()) {
            return !login.contains(" ");
        }
        log.info("Login is null or blank. Validation succeed");
        return true;
    }
}
