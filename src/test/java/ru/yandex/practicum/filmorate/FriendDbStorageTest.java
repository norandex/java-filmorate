package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.impl.FriendDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageTest {

    private final JdbcTemplate jdbcTemplate;


    @Test
    public void testFindUserById() {
        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        FriendDao friendDao = new FriendDaoImpl(jdbcTemplate, userStorage);

        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).build();
        User anotherUser = User.builder().email("user@yahoo.ru").login("ilya123").name("Ilya Petrov").birthday(LocalDate.of(1992, 1, 1)).build();

        userStorage.createUser(newUser);
        userStorage.createUser(anotherUser);

        friendDao.addFriend(newUser.getId(), anotherUser.getId());

        List<User> friendList = friendDao.getFriendsOfUser(newUser.getId());

        Assertions.assertThat(friendList).contains(anotherUser);
    }

    @Test
    public void testGetCommonFriends() {
        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        FriendDao friendDao = new FriendDaoImpl(jdbcTemplate, userStorage);

        User user = User.builder().email("user@gmail.com").login("petya228").name("Petya Petrov").birthday(LocalDate.of(1960, 1, 1)).build();
        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).build();
        User anotherUser = User.builder().email("user@yahoo.ru").login("ilya123").name("Ilya Petrov").birthday(LocalDate.of(1992, 1, 1)).build();

        userStorage.createUser(user);
        userStorage.createUser(newUser);
        userStorage.createUser(anotherUser);

        friendDao.addFriend(newUser.getId(), anotherUser.getId());
        friendDao.addFriend(user.getId(), anotherUser.getId());

        List<User> friendList = friendDao.getCommonFriends(newUser.getId(), user.getId());

        Assertions.assertThat(friendList).contains(anotherUser);
    }

    @Test
    public void testDeleteFriends() {
        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        FriendDao friendDao = new FriendDaoImpl(jdbcTemplate, userStorage);

        User user = User.builder().email("user@gmail.com").login("petya228").name("Petya Petrov").birthday(LocalDate.of(1960, 1, 1)).build();
        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).build();
        User anotherUser = User.builder().email("user@yahoo.ru").login("ilya123").name("Ilya Petrov").birthday(LocalDate.of(1992, 1, 1)).build();

        userStorage.createUser(user);
        userStorage.createUser(newUser);
        userStorage.createUser(anotherUser);

        friendDao.addFriend(user.getId(), newUser.getId());
        friendDao.addFriend(user.getId(), anotherUser.getId());

        friendDao.deleteFriends(user.getId(), anotherUser.getId());

        List<User> friendList = friendDao.getFriendsOfUser(user.getId());

        Assertions.assertThat(friendList).containsOnly(newUser);
    }

}
