package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.InvalidUserUpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserStorage;

import java.time.LocalDate;
import java.time.Month;

public class UserStorageTest {

    UserStorage us;

    @BeforeEach
    public void createFilmStorage() {
        this.us = new UserStorage();
    }

    @Test
    public void testInvalidFilmUpdateException() {
        User user = new User(1, "Name", "login", LocalDate.of(2000, Month.DECEMBER, 28));
        us.createUser(user);
        User user_unknown_id = new User(22, "Name", "login", LocalDate.of(2000, Month.DECEMBER, 28));
        Assertions.assertThrows(InvalidUserUpdateException.class, () -> us.updateUser(user_unknown_id));
    }
}
