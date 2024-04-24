package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("User > Get All Request");
        return userService.readAllUsers();
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long userId) {
        log.info("User > get friends of user with id = {}", userId);
        return userService.getFriendsOfUser(userId);
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("User > Post Request {}", user);
        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User put(@Valid @RequestBody User user) {
        log.info("User > Put Request {}", user);
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping(value = "/users/{id}/friends/{userId}")
    public User makeFriends(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("User > make friends {} -> {}: ", id, userId);
        return userService.makeFriends(id, userId);
    }
}
