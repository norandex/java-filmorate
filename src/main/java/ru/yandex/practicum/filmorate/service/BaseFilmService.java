package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LocalDate sCinemaBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public BaseFilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film create(Film film) {
        validate(film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.readFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Invalid film id=%s", id.toString()));
        }
        return film;
    }

    @Override
    public Film deleteFilmById(Long id) {
        getFilmById(id);
        return filmStorage.deleteFilm(id);
    }

    @Override
    public List<Film> readAllFilms() {
        return filmStorage.readAllFilms();
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        getFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film likeFilm(Long filmId, Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Invalid user id=%s", userId.toString()));
        }
        Film film = getFilmById(filmId);
        return addLikeToFilm(film, userId);
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Invalid user id=%s", userId.toString()));
        }
        return removeLikeFromFilm(film, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count < 0) throw new IncorrectParameterException("count");
        return filmStorage.readAllFilms().stream().sorted(Comparator.comparing(o -> -o.getLikes().size())).limit(count).collect(Collectors.toList());
    }

    private Film addLikeToFilm(Film film, Long userId) {
        film.getLikes().add(userId);
        return film;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(sCinemaBirthday)) {
            throw new ValidationException();
        }
    }

    private Film removeLikeFromFilm(Film film, Long userId) {
        film.getLikes().remove(userId);
        return film;
    }


}
