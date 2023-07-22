package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Markers;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Getter
    private UserStorage storage;

    @Autowired
    public void setStorage(UserStorage storage) {
        this.storage = storage;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return storage.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return storage.create(user);
    }

    @PutMapping
    @Validated(Markers.OnUpdate.class)
    public User updateUser(@Valid @RequestBody User user) {
        return storage.update(user);
    }
}
