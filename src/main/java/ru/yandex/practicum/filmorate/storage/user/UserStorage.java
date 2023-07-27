package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
    User create(User user);

    User retrieve(int id);

    User update(User user);

    List<User> findAll();
}
