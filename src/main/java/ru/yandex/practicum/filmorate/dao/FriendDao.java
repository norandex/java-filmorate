package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {
    List<User> getFriendsOfUser(Long id);

    User deleteFriends(Long id, Long friendId);

    User addFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long id, Long otherId);
}
