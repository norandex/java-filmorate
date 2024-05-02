package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Primary
@Slf4j
public class DbUserService implements UserService {

    private final UserStorage userDao;
    private final FriendDao friendDao;

    @Autowired
    public DbUserService(UserStorage userDao, FriendDao friendDao) {
        this.userDao = userDao;
        this.friendDao = friendDao;
    }

    @Override
    public User create(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        getUserById(userId);
        getUserById(otherId);
        return friendDao.getCommonFriends(userId, otherId);
    }

    @Override
    public List<User> readAllUsers() {
        return userDao.readAllUsers();
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userDao.updateUser(user);
        if (updatedUser == null) {
            throw new UserNotFoundException("Invalid user update");
        }
        return updatedUser;
    }

    @Override
    public User makeFriends(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        return friendDao.addFriend(userId, friendId);
    }

    @Override
    public User deleteFriends(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);
        return friendDao.deleteFriends(userId, friendId);
    }

    @Override
    public List<User> getFriendsOfUser(Long id) {
        getUserById(id);
        return friendDao.getFriendsOfUser(id);
    }
}
