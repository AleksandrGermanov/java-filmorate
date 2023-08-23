package ru.yandex.practicum.filmorate.storage.FriendshipRequest;

import ru.yandex.practicum.filmorate.model.FriendshipRequest;

public interface FriendshipRequestStorage {
    FriendshipRequest upsert(FriendshipRequest request);
}
