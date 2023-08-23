package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Endpoint /users} invoked (GET)");
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Endpoint /users} invoked (POST)");
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Endpoint /users} invoked (PUT)");
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User retrieve(@PathVariable int id) {
        log.info("Endpoint /users/{id}} invoked (GET)");
        return userService.retrieve(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable int id) {
        log.info("Endpoint /users/{id}/friends} invoked (GET)");
        return userService.findAllFriends(userService.retrieve(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable int id,
                                        @PathVariable int otherId) {
        log.info("Endpoint /users/{id}/friends/common/{otherId}} invoked (GET)");
        return userService.findCommonFriends(userService.retrieve(id),
                userService.retrieve(otherId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void befriend(
            @PathVariable int id,
            @PathVariable int friendId) {
        log.info("Endpoint /users/{id}/friends/{friendId}} invoked (PUT)");
        UserServiceImpl service = (UserServiceImpl) userService;
        service.befriendAndAccept(userService.retrieve(friendId), userService.retrieve(id));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void unfriend(
            @PathVariable int id,
            @PathVariable int friendId) {
        log.info("Endpoint /users/{id}/friends/{friendId}} invoked (DELETE)");
        userService.unfriend(userService.retrieve(id), userService.retrieve(friendId));
    }
}