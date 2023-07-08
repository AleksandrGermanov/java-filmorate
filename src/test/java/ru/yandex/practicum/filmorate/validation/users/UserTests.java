package ru.yandex.practicum.filmorate.validation.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.validation.Markers;
import ru.yandex.practicum.filmorate.validation.ValidatorForTests;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    private final ValidatorForTests<User> userValidator = new ValidatorForTests<>();
    private User user;

    @BeforeEach
    public void createUser() {
        user = new User();

        user.setName("Name");
        user.setLogin("login");
        user.setEmail("e@mail.fake");
        user.setBirthday(LocalDate.of(2023, 07, 07));
    }

    @Test
    void validateEmptyUser() {
        user = new User();
        assertThrows(ConstraintViolationException.class, () -> userValidator.isParameterValid(user));
    }

    @Test
    void validateDefaultUser() {
        assertTrue(userValidator.isParameterValid(user));
    }

    @Test
    void validateUserWithUnknownIdOnUpdate(){
        user.setId(-1);
        assertThrows(ConstraintViolationException.class,
                () -> userValidator.isParameterValid(user, Markers.OnUpdate.class));
    }

    @Test
    void validateUserWithKnownIdOnUpdate(){
        UserController uc = new UserController();

        uc.createUser(user);
        user.setName("Updated");
        assertDoesNotThrow(() -> userValidator.isParameterValid(user, Markers.OnUpdate.class));
    }

    @Test
    void validateUserWithNullName() {
        user.setName(null);
        assertTrue(userValidator.isParameterValid(user));
    }

    @Test
    void validateUserWithBlankLogin() {
        user.setLogin(" \n");
        assertThrows(ConstraintViolationException.class, () -> userValidator.isParameterValid(user));
    }

    @Test
    void validateUserWithWrongEmail() {
        user.setEmail("wrong");
        assertThrows(ConstraintViolationException.class, () -> userValidator.isParameterValid(user));
    }

    @Test
    void validateUserWithWrongBirthday() {
        user.setBirthday(LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> userValidator.isParameterValid(user));
    }
}

