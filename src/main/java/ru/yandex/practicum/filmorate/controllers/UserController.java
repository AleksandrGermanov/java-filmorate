package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

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
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User retrieve(@PathVariable int id) {
        return userService.retrieve(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable int id) {
        return userService.findAllFriends(userService.retrieve(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable int id,
                                        @PathVariable int otherId) {
        return userService.findCommonFriends(userService.retrieve(id),
                userService.retrieve(otherId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void befriend(
            @PathVariable int id,
            @PathVariable int friendId) {
        userService.befriend(userService.retrieve(id), userService.retrieve(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void unfriend(
            @PathVariable int id,
            @PathVariable int friendId) {
        userService.unfriend(userService.retrieve(id), userService.retrieve(friendId));
    }
}