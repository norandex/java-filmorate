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
public class BaseUserService implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public BaseUserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("Invalid user id=%s", id.toString()));
        }
        return user;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = getUserById(id);
        User userOther = getUserById(otherId);
        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(userOther.getFriends());
        return intersection.stream().map(this::getUserById).collect(Collectors.toList());
    }

    @Override
    public List<User> readAllUsers() {
        return userStorage.readAllUsers();
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public User makeFriends(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        addFriendToUser(user, friend.getId());
        addFriendToUser(friend, user.getId());
        return user;
    }

    @Override
    public User deleteFriends(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        removeFriendFromUser(user, friend.getId());
        removeFriendFromUser(friend, user.getId());
        return user;
    }

    @Override
    public List<User> getFriendsOfUser(Long id) {
        User user = getUserById(id);
        return user.getFriends().stream().map(this::getUserById).collect(Collectors.toList());
    }

    private User addFriendToUser(User user, Long id) {
        user.getFriends().add(id);
        return user;
    }

    private User removeFriendFromUser(User user, Long id) {
        user.getFriends().remove(id);
        return user;
    }

}
