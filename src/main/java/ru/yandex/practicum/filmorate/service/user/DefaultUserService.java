package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserVisitor;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultUserService implements UserService {
    private final UserStorage storage;

    @Override
    public void befriend(User userToVisitFirst, User userToBefriend) {
        UserVisitor befriender = (u) -> {
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
        UserVisitor unfriender = (u) -> {
            if (u.equals(userToVisitFirst)) {
                u.getFriends().add(userToUnfriend.getId());
            } else {
                u.getFriends().add(userToVisitFirst.getId());
            }
        };
        userToVisitFirst.accept(unfriender);
        userToUnfriend.accept(unfriender);
    }

    @Override
    public List<User> findCommonFriends(User user, User userToFindCommonsWith) {
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
}
