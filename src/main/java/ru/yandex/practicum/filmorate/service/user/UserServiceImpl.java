package ru.yandex.practicum.filmorate.service.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.MatchesNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Getter
    private final UserStorage storage;

    @Override
    public void befriend(User userToVisitFirst, User userToBefriend) {
        validateId(userToVisitFirst);
        validateId(userToBefriend);
        Visitor<User> befriender = (u) -> {
            if (u.equals(userToVisitFirst)) {
                u.getFriends().add(userToBefriend.getId());
            } else {
                u.getFriends().add(userToVisitFirst.getId());
            }
        };
        userToVisitFirst.accept(befriender);
        userToBefriend.accept(befriender);
    }

    @Override
    public void unfriend(User userToVisitFirst, User userToUnfriend) {
        validateId(userToVisitFirst);
        validateId(userToUnfriend);
        Visitor<User> unfriender = (u) -> {
            if (u.equals(userToVisitFirst)) {
                u.getFriends().remove(userToUnfriend.getId());
            } else {
                u.getFriends().remove(userToVisitFirst.getId());
            }
        };
        userToVisitFirst.accept(unfriender);
        userToUnfriend.accept(unfriender);
    }

    @Override
    public List<User> findCommonFriends(User user, User userToFindCommonsWith) {
        validateId(user);
        validateId(userToFindCommonsWith);
        return user.getFriends().stream()
                .filter(id -> userToFindCommonsWith.getFriends().contains(id))
                .map(storage::retrieve)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllFriends(User user) {
        return user.getFriends().stream()
                .map(storage::retrieve)
                .collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        return storage.create(user);
    }

    @Override
    public User retrieve(int id) {
        validateId(id);
        return storage.retrieve(id);
    }

    @Override
    public User update(User user) {
        validateId(user);
        return storage.update(user);
    }

    @Override
    public List<User> findAll() {
        return storage.findAll();
    }

    @Override
    public boolean validateId(int id) {
        boolean value = storage.findAll().stream()
                .map(User::getId)
                .collect(Collectors.toSet())
                .contains(id);
        if (!value) {
            throw new MatchesNotFoundException("Пользователь с id " + id + " не найден.");
        }
        return true;
    }

    @Override
    public boolean validateId(User user) {
        return validateId(user.getId());
    }
}
