package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.validation.IsInSet;
import ru.yandex.practicum.filmorate.service.validation.Markers;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserStorage storage;
    private final UserService service;

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

    @GetMapping("/{id}")
    public User retrieve(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id) {
        return storage.retrieve(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id) {
        return service.findAllFriends(storage.retrieve(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int otherId) {
        return service.findCommonFriends(storage.retrieve(id),
                storage.retrieve(otherId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void befriend(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int friendId) {
        service.befriend(storage.retrieve(id), storage.retrieve(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void unfriend(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int friendId) {
        service.unfriend(storage.retrieve(id), storage.retrieve(friendId));
    }
}

