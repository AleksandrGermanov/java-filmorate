package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Markers;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Getter
    private final static Map<Integer, User> users = new HashMap<>();
    private static int idCounter; //пока нет более внятного присвоения id

    @GetMapping
    public List<User> getAllUsers() {
        log.info("GET /users is successfully proceed.");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        user.setId(++idCounter);
        log.info("POST /users is successfully proceed. User id = " + user.getId() + ".");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    @Validated(Markers.OnUpdate.class)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users is successfully proceed. User id = " + user.getId() + ".");
        users.put(user.getId(), user);
        return user;
    }
}
