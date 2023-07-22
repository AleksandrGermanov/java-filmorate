package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private static final Map<Integer, User> users = new HashMap<>();
    private static int idCounter; //пока нет более внятного присвоения id

    @Override
    public User create(User user) {
        user.setId(generateId());
        log.info("POST /users is successfully proceed. User id = " + user.getId() + ".");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User retrieve(int id) {
        //TODO заглушка!
        return null;
    }

    @Override
    public User update(User user) {
        log.info("PUT /users is successfully proceed. User id = " + user.getId() + ".");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        log.info("GET /users is successfully proceed.");
        return new ArrayList<>(users.values());
    }

    private int generateId() {
        return ++idCounter;
    }
}

