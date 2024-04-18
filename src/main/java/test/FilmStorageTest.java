package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.InvalidFilmReleaseDateException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmUpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmStorage;

import java.time.LocalDate;
import java.time.Month;

public class FilmStorageTest {

    FilmStorage fs;

    @BeforeEach
    public void createFilmStorage() {
        this.fs = new FilmStorage();
    }

    @Test
    public void testInvalidFilmDateException() {
        Film film = new Film(1, "Name", "Description", 120, LocalDate.of(1892, Month.DECEMBER, 28));

        Assertions.assertThrows(InvalidFilmReleaseDateException.class, () -> fs.createFilm(film));
    }

    @Test
    public void testInvalidFilmUpdateException() {
        Film film = new Film(1, "Name", "Description", 120, LocalDate.of(2000, Month.DECEMBER, 28));
        fs.createFilm(film);
        Film film_unknown_id = new Film(22, "Name", "Description", 120, LocalDate.of(2000, Month.DECEMBER, 28));
        Assertions.assertThrows(InvalidFilmUpdateException.class, () -> fs.updateFilm(film_unknown_id));
    }
}
