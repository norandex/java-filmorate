package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {


    private final JdbcTemplate jdbcTemplate;
    MpaRating mpa = new MpaRating(1L, "G");

    @Test
    public void testFindUserById() {
        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        Film newFilm = Film.builder().id(1L).name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(mpa).genres(new HashSet<>()).build();

        filmStorage.createFilm(newFilm);


        Film savedFilm = filmStorage.readFilmById(1L);


        Assertions.assertThat(savedFilm).isNotNull().usingRecursiveComparison().isEqualTo(newFilm);
    }

    @Test
    public void testUpdateFilm() {
        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        Film newFilm = Film.builder().id(1L).name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(mpa).genres(new HashSet<>()).build();

        filmStorage.createFilm(newFilm);

        Film savedFilm = filmStorage.readFilmById(1L);

        Film newFilmUpdated = Film.builder().id(1L).name("test_film 2").description("great film, pt.2").duration(144).releaseDate(LocalDate.of(1995, 1, 1)).mpa(mpa).genres(new HashSet<>()).build();

        Film updatedFilm = filmStorage.updateFilm(newFilmUpdated);

        Assertions.assertThat(updatedFilm).isNotNull().usingRecursiveComparison().isEqualTo(newFilmUpdated);

    }

}