package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    List<User> readAllUsers();

    User getUserById(Long id);

    User updateUser(User user);

    boolean deleteUser(Long id);

}
