package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).friends(null).build();
        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        userStorage.createUser(newUser);

        User savedUser = userStorage.getUserById(newUser.getId());

        Assertions.assertThat(savedUser).isNotNull().usingRecursiveComparison().isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).friends(null).build();

        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        userStorage.createUser(newUser);

        User savedUser = userStorage.getUserById(newUser.getId());

        User newUpdatedUser = User.builder().id(newUser.getId()).email("user@yahoo.com").login("nevanya123").name("Neivan Petrov").birthday(LocalDate.of(1997, 1, 1)).friends(null).build();

        User updatedUser = userStorage.updateUser(newUpdatedUser);

        Assertions.assertThat(updatedUser).isNotNull().usingRecursiveComparison().isEqualTo(newUpdatedUser);

    }

    @Test
    public void testDeleteUser() {
        User newUser = User.builder().id(1L).email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).friends(null).build();

        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        userStorage.createUser(newUser);
        userStorage.deleteUser(newUser.getId());

        List<User> userList = userStorage.readAllUsers();

        Assertions.assertThat(userList).isEmpty();
    }

    @Test
    public void testGetAllUser() {
        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);

        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).friends(null).build();
        User anotherUser = User.builder().email("user@yahoo.ru").login("ilya123").name("Ilya Petrov").birthday(LocalDate.of(1992, 1, 1)).friends(null).build();

        userStorage.createUser(newUser);
        userStorage.createUser(anotherUser);

        List<User> listUser = List.of(newUser, anotherUser);

        List<User> readUserList = userStorage.readAllUsers();

        Assertions.assertThat(listUser).hasSameElementsAs(readUserList);
    }

}