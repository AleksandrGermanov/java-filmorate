package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service

public interface UserService {
    void befriend(User userToVisit, User userToBefriend);

    void unfriend(User userToVisit, User userToUnfriend);

    List<User> findCommonFriends(User user, User userToFindCommonsWith);

    List<User> findAllFriends(User user);

    boolean validateId(int id);

    boolean validateId(User user);

    User create(User user);

    User retrieve(int id);

    User update(User user);

    List<User> findAll();
}
