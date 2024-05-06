package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.LikeDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testPopularFilms() {
        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        UserStorage userStorage = new UserDaoImpl(jdbcTemplate);
        LikeDao likeDao = new LikeDaoImpl(jdbcTemplate, userStorage, filmStorage);

        User newUser = User.builder().email("user@email.ru").login("vanya123").name("Ivan Petrov").birthday(LocalDate.of(1990, 1, 1)).friends(null).build();
        userStorage.createUser(newUser);

        User newSecondUser = User.builder().email("user@yahoo.ru").login("vanya1444").name("Petya Petrov").birthday(LocalDate.of(1991, 1, 1)).friends(null).build();
        userStorage.createUser(newSecondUser);

        User newThirdUser = User.builder().email("user@gmail.ru").login("Kesha1444").name("Kesha Petrov").birthday(LocalDate.of(1991, 1, 1)).friends(null).build();
        userStorage.createUser(newThirdUser);

        Film newFilm = Film.builder().name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(new MpaRating(1L, "G")).genres(new HashSet<>()).build();
        filmStorage.createFilm(newFilm);

        Film newSecondFilm = Film.builder().name("test_film2").description("great film2").duration(120).releaseDate(LocalDate.of(1991, 1, 1)).mpa(new MpaRating(1L, "G")).genres(new HashSet<>()).build();
        filmStorage.createFilm(newSecondFilm);

        Film newThirdFilm = Film.builder().name("test_film3").description("great film3").duration(120).releaseDate(LocalDate.of(1992, 1, 1)).mpa(new MpaRating(1L, "G")).genres(new HashSet<>()).build();
        filmStorage.createFilm(newThirdFilm);

        likeDao.likeFilm(newThirdFilm.getId(), newThirdUser.getId());
        likeDao.likeFilm(newThirdFilm.getId(), newSecondUser.getId());
        likeDao.likeFilm(newThirdFilm.getId(), newUser.getId());

        likeDao.likeFilm(newSecondFilm.getId(), newThirdUser.getId());

        likeDao.likeFilm(newFilm.getId(), newThirdUser.getId());
        likeDao.likeFilm(newFilm.getId(), newSecondUser.getId());

        List<Film> popFilms = likeDao.getPopularFilms(10);

        Assertions.assertThat(popFilms).containsExactly(newThirdFilm, newFilm, newSecondFilm);

    }
}
