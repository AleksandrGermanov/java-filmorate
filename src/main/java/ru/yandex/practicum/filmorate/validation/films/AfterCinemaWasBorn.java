package ru.yandex.practicum.filmorate.validation.films;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmLocalDateValidator.class)
public @interface AfterCinemaWasBorn {
String message() default "Дата не может быть ранее 28 декабря 1895г.";
Class<?>[] groups() default {};
Class<? extends Payload>[] payload() default {};
}
