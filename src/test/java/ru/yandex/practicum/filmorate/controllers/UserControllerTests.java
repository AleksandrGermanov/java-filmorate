package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserControllerTests {
    UserController uc = new UserController();
    User user;

    @BeforeEach
    public void createFilm() {
        user = new User();

        user.setName("Name");
        user.setLogin("login");
        user.setEmail("e@mail.fake");
        user.setBirthday(LocalDate.of(2023, 07, 07));
    }

    @Test
    void getAllUsersReturnsArrayList() {
        assertEquals(ArrayList.class, uc.getAllUsers().getClass());
    }

    @Test
    void controllerSetsIdOnCreation() {
        int oldId = user.getId();

        uc.createUser(user);
        assertNotEquals(oldId, user.getId());
    }

    @Test
    void controllerPutsUserToMapOnCreation() {
        uc.createUser(user);
        assertEquals(user, UserController.getUsers().get(user.getId()));
    }

    @Test
    void controllerReturnsSameUserAsInMapOnCreation() {
        assertEquals(uc.createUser(user), UserController.getUsers().get(user.getId()));
    }

    @Test
    void controllerSetsLoginValueAsNameIfNameIsNullOnCreation() {
        user.setName(null);
        uc.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void controllerReturnsSameUserAsInMapOnUpdate() {
        assertEquals(uc.updateUser(user), UserController.getUsers().get(user.getId()));
    }
}
