package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    Genre createGenre(Genre genre);

    List<Genre> readAllGenres();

    Genre readGenreById(Long id);

    Genre updateGenre(Genre genre);

    boolean deleteGenre(Long id);
}
