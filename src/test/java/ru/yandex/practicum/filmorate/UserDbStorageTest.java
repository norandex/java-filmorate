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
        User newUser = User.builder().id(1L).email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).friends(null).build();

        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        userStorage.createUser(newUser);

        User savedUser = userStorage.getUserById(1L);

        User newUpdatedUser = User.builder().id(1L).email("user@yahoo.com").login("nevanya123").name("Neivan Petrov").birthday(LocalDate.of(1997, 1, 1)).friends(null).build();

        User updatedUser = userStorage.updateUser(newUpdatedUser);

        Assertions.assertThat(updatedUser).isNotNull().usingRecursiveComparison().isEqualTo(newUpdatedUser);

    }
}