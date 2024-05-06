package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRating mpa = new MpaRating(1L, "G");

    @Test
    public void testFindFilmById() {
        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        Film newFilm = Film.builder().name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(mpa).genres(new HashSet<>(List.of(new Genre(5L, "Документальный")))).build();

        filmStorage.createFilm(newFilm);


        Film savedFilm = filmStorage.readFilmById(newFilm.getId());


        Assertions.assertThat(savedFilm).isNotNull().usingRecursiveComparison().isEqualTo(newFilm);
    }

    @Test
    public void testUpdateFilm() {
        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        Film newFilm = Film.builder().id(1L).name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(mpa).genres(new HashSet<>(List.of(new Genre(5L, "Документальный")))).build();

        filmStorage.createFilm(newFilm);

        Film savedFilm = filmStorage.readFilmById(1L);

        Film newFilmUpdated = Film.builder().id(1L).name("test_film 2").description("great film, pt.2").duration(144).releaseDate(LocalDate.of(1995, 1, 1)).mpa(mpa).genres(new HashSet<>(List.of(new Genre(6L, "Боевик")))).build();

        Film updatedFilm = filmStorage.updateFilm(newFilmUpdated);

        Assertions.assertThat(updatedFilm).isNotNull().usingRecursiveComparison().isEqualTo(newFilmUpdated);

    }

    @Test
    public void testReadAllFilms() {
        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        Film newFilm = Film.builder().name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(mpa).genres(new HashSet<>(List.of(new Genre(6L, "Боевик")))).build();

        filmStorage.createFilm(newFilm);

        System.out.println(filmStorage.readFilmById(newFilm.getId()));

        Film otherFilm = Film.builder().name("test_film 2").description("great film, pt.2").duration(144).releaseDate(LocalDate.of(1995, 1, 1)).mpa(mpa).genres(new HashSet<>(List.of(new Genre(5L, "Документальный")))).build();

        filmStorage.createFilm(otherFilm);

        System.out.println(filmStorage.readAllFilms());

        List<Film> readFilms = filmStorage.readAllFilms();

        List<Film> listFilms = List.of(newFilm, otherFilm);

        Assertions.assertThat(readFilms).hasSameElementsAs(listFilms);

    }

    @Test
    public void testDeleteFilm() {
        Film newFilm = Film.builder().name("test_film").description("great film").duration(120).releaseDate(LocalDate.of(1990, 1, 1)).mpa(mpa).genres(new HashSet<>(List.of(new Genre(6L, "Боевик")))).build();

        FilmStorage filmStorage = new FilmDaoImpl(jdbcTemplate);
        filmStorage.createFilm(newFilm);
        filmStorage.deleteFilm(newFilm.getId());

        List<Film> filmList = filmStorage.readAllFilms();

        Assertions.assertThat(filmList).isEmpty();
    }

}