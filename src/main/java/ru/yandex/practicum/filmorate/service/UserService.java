package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.createUser(user);
    }

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Invalid user id=%s", id.toString()));
        }
        return user;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = getUserById(id);
        User userOther = getUserById(otherId);
        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(userOther.getFriends());
        return intersection.stream().map(this::getUserById).collect(Collectors.toList());
    }

    public List<User> readAllUsers() {
        return userStorage.readAllUsers();
    }

    public User updateUser(User user) {
        getUserById(user.getId());
        return userStorage.updateUser(user);
    }

    public User makeFriends(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friend.getId());
        friend.addFriend(user.getId());
        return user;
    }

    public User deleteFriends(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.removeFriend(friend.getId());
        friend.removeFriend(user.getId());
        return user;
    }

    public List<User> getFriendsOfUser(Long id) {
        User user = getUserById(id);
        return user.getFriends().stream().map(this::getUserById).collect(Collectors.toList());
    }
}
