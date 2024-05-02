package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeDao {

    Film likeFilm(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    List<Film> getPopularFilms(Integer count);
}
