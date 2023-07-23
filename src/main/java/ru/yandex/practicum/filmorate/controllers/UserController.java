package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.validation.IsInSet;
import ru.yandex.practicum.filmorate.service.validation.Markers;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getStorage().findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.getStorage().create(user);
    }

    @PutMapping
    @Validated(Markers.OnUpdate.class)
    public User updateUser(@Valid @RequestBody User user) {
        return userService.getStorage().update(user);
    }

    @GetMapping("/{id}")
    public User retrieve(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id) {
        return userService.getStorage().retrieve(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id) {
        return userService.findAllFriends(userService.getStorage().retrieve(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int otherId) {
        return userService.findCommonFriends(userService.getStorage().retrieve(id),
                userService.getStorage().retrieve(otherId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void befriend(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int friendId) {
        userService.befriend(userService.getStorage().retrieve(id), userService.getStorage().retrieve(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void unfriend(
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int id,
            @IsInSet(setHolder = InMemoryUserStorage.class) @PathVariable int friendId) {
        userService.unfriend(userService.getStorage().retrieve(id), userService.getStorage().retrieve(friendId));
    }
}