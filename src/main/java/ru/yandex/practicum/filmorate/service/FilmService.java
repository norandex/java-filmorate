package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film getFilmById(Long id);

    boolean deleteFilmById(Long id);

    List<Film> readAllFilms();

    Film updateFilm(Film film);

    Film likeFilm(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(Integer count);
}
