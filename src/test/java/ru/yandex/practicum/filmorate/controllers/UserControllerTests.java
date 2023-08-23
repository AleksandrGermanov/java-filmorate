package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTests {
    private final UserController uc;
    User user;

    @BeforeEach
    public void createUser() {
        user = new User();

        user.setName("Name");
        user.setLogin("login");
        user.setEmail("e@mail.fake");
        user.setBirthday(LocalDate.of(2023, 7, 7));
    }


    @Test
    @Sql(statements = "DELETE FROM users")
    void getAllUsersReturnsArrayList() {
        assertEquals(ArrayList.class, uc.getAllUsers().getClass());
    }

    @Test
    @Sql(statements = "DELETE FROM users")
    void controllerSetsIdOnCreation() {
        assertNull(user.getId());
        assertNotNull(uc.createUser(user).getId());
    }

    @Test
    @Sql(statements = "DELETE FROM users")
    void controllerPutsUserToMapOnCreation() {
        User u = uc.createUser(user);
        assertTrue(uc.getAllUsers().contains(u));
    }

    @Test
    void controllerSetsLoginValueAsNameIfNameIsNullOnCreation() {
        user.setName(null);
        uc.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @Sql(statements = "DELETE FROM users")
    void controllerReturnsSameUserAsInMapOnUpdate() {
        User u = uc.createUser(user);
        u.setLogin("filmaniac");
        assertEquals(uc.updateUser(u).getLogin(), u.getLogin());
    }
}
