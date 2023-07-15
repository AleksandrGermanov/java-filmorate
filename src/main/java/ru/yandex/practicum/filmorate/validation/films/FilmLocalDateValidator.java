package ru.yandex.practicum.filmorate.validation.films;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class FilmLocalDateValidator
        implements ConstraintValidator<AfterCinemaWasBorn, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date != null) {
            return date.isAfter(LocalDate.of(1895, 12, 28));
        }
        log.info("Date is null. Validation succeed.");
        return true;
    }
}
