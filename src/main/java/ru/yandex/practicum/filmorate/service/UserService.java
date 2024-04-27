package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User getUserById(Long id);
    List<User> getCommonFriends(Long id, Long otherId);
    List<User> readAllUsers();
    User updateUser(User user);
    User makeFriends(Long userId, Long friendId);
    User deleteFriends(Long userId, Long friendId);
    List<User> getFriendsOfUser(Long id);
}
