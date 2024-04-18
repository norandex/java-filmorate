package ru.yandex.practicum.filmorate.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage films = new FilmStorage();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Film > Get All Request");
        return films.readAllFilms();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Film > Post Request");
        return films.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film put(@Valid @RequestBody Film film) {
        log.info("Film > Put Request");
        return films.updateFilm(film);
    }
}
