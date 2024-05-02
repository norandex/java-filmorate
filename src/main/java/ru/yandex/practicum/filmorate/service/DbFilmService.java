package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@Primary
public class DbFilmService implements FilmService {

    private final FilmStorage filmDao;
    private final LikeDao likeDao;

    private final LocalDate sCinemaBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public DbFilmService(FilmStorage filmDao, LikeDao likeDao) {
        this.filmDao = filmDao;
        this.likeDao = likeDao;
    }

    @Override
    public Film create(Film film) {
        validate(film);
        return filmDao.createFilm(film);
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmDao.readFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public boolean deleteFilmById(Long id) {
        return filmDao.deleteFilm(id);
    }

    @Override
    public List<Film> readAllFilms() {
        return filmDao.readAllFilms();
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        return filmDao.updateFilm(film);
    }

    @Override
    public Film likeFilm(Long filmId, Long userId) {
        return likeDao.likeFilm(filmId, userId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        return likeDao.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return likeDao.getPopularFilms(count);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(sCinemaBirthday)) {
            throw new ValidationException();
        }
    }
}
