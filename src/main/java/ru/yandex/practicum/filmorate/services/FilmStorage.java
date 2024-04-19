package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.exception.InvalidFilmReleaseDateException;
import ru.yandex.practicum.filmorate.exception.InvalidFilmUpdateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate sCinemaBirthday = LocalDate.of(1895, Month.DECEMBER, 28);
    private int id = 1;

    public Film createFilm(Film film) {
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> readAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film updateFilm(Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            throw new InvalidFilmUpdateException();
        }
        films.put(film.getId(), film);
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(sCinemaBirthday)) {
            throw new InvalidFilmReleaseDateException();
        }
    }
}
