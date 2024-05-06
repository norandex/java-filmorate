package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Film > Get All Request");
        return filmService.readAllFilms();
    }

    @GetMapping("/films/{filmId}")
    public Film findById(@PathVariable("filmId") Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Film > Post Request {}", film);
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film put(@Valid @RequestBody Film film) {
        log.info("Film > Put Request {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film putLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Film > Put Like {} -> {}", filmId, userId);
        return filmService.likeFilm(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.deleteLike(filmId, userId);
    }


}
