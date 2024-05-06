package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {

    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(Long id) {
        Genre genre = genreDao.readGenreById(id);
        if (genre == null) {
            throw new GenreNotFoundException("Жанр не найден");
        }
        return genre;
    }

    public List<Genre> readAllGenres() {
        return genreDao.readAllGenres();
    }

}
