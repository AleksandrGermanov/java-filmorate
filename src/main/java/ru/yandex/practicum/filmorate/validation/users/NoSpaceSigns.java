package ru.yandex.practicum.filmorate.validation.users;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserLoginValidator.class)
public @interface NoSpaceSigns {
    String message() default "Логин не должен содержать пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
