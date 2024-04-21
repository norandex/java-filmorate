package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserStorage users = new UserStorage();

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("User > Get All Request");
        return users.readAllUsers();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("User > Post Request {}", user);
        users.createUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User put(@Valid @RequestBody User user) {
        log.info("User > Put Request {}", user);
        users.updateUser(user);
        return user;
    }
}
