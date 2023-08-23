package ru.yandex.practicum.filmorate.service.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.MatchesNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.storage.FriendshipRequest.FriendshipRequestStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final FriendshipRequestStorage requestStorage;
    @Getter
    private final UserStorage storage;

    @Override
    public void befriend(User requestExporter, User requestImporter) {
        log.info("Proceeding befriend() with exp = " + requestExporter.getId() + " and " +
                "imp = " + requestImporter.getId() + ".");
        validateId(requestExporter);
        validateId(requestImporter);
        FriendshipRequest request = new FriendshipRequest(requestExporter.getId(), requestImporter.getId(), 1);
        Visitor<User> befriender = u -> u.getRequests().add(request);

        requestStorage.upsert(request);
        requestExporter.accept(befriender);
        requestImporter.accept(befriender);
    }

    public void befriendAndAccept(User requestExporter, User requestImporter) {
        log.info("Proceeding befriendAndAccept() with exp = " + requestExporter.getId() + " and " +
                "imp = " + requestImporter.getId() + ".");
        validateId(requestExporter);
        validateId(requestImporter);
        FriendshipRequest request = new FriendshipRequest(requestExporter.getId(), requestImporter.getId(), 2);
        Visitor<User> befriender = u -> u.getRequests().add(request);

        requestStorage.upsert(request);
        requestExporter.accept(befriender);
        requestImporter.accept(befriender);
    }

    @Override
    public void unfriend(User whoUnfriends, User toBeUnfriended) {
        log.info("Proceeding unfriend() with whoUnfriends = " + whoUnfriends.getId() + " and "
                + "toBeUnfriended = " + toBeUnfriended.getId() + ".");
        validateId(whoUnfriends);
        validateId(toBeUnfriended);
        Visitor<User> unfriender = u -> {
            Optional<FriendshipRequest> requestOpt = u.getRequests().stream().filter(req ->
                            (whoUnfriends.getId().equals(req.getImporterId())
                                    && toBeUnfriended.getId().equals(req.getExporterId()))
                    )
                    .findFirst();
            if (requestOpt.isPresent()) {
                FriendshipRequest request = requestOpt.get();
                request.setStatusId(3);
                requestStorage.upsert(request);
            }
            u.getFriends().remove(toBeUnfriended.getId());
        };
        whoUnfriends.accept(unfriender);
    }

    @Override
    public List<User> findCommonFriends(User user, User userToFindCommonsWith) {
        log.info("Proceeding findCommonFriends() with user = " + user.getId() + " and "
                + "userToFindCommonsWith = " + userToFindCommonsWith.getId() + ".");
        validateId(user);
        validateId(userToFindCommonsWith);
        return user.getFriends().stream()
                .filter(id -> userToFindCommonsWith.getFriends().contains(id))
                .map(storage::retrieve)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllFriends(User user) {
        log.info("Proceeding findAllFriends() with user = " + user.getId() + ".");
        return user.getFriends().stream()
                .map(storage::retrieve)
                .collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        log.info("Proceeding create() with user = " + user.getId() + ".");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.create(user);
    }

    @Override
    public User retrieve(int id) {
        log.info("Proceeding retrieve() with id = " + id + ".");
        validateId(id);
        return storage.retrieve(id);
    }

    @Override
    public User update(User user) {
        log.info("Proceeding update() with user = " + user.getId() + ".");
        validateId(user);
        return storage.update(user);
    }

    @Override
    public List<User> findAll() {
        log.info("Proceeding findAll().");
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
