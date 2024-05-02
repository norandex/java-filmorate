package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
public class FilmDaoImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into films(name, description, duration, date_release, mpa_id) " +
                "values (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setLong(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        // для удовлетворения последнего теста из postmana
        if (!(film.getGenres() == null)) {
            List<Genre> genres = new ArrayList<>(film.getGenres());
            genres.sort(Comparator.comparing(Genre::getId));
            film.setGenres(new LinkedHashSet<>(genres));
            addFilmGenres(film);
        }

        return film;
    }

    @Override
    public List<Film> readAllFilms() {
        String sqlQuery = "SELECT f.id film_id, f.name name, f.description description, f.duration duration, f.date_release date_release, mr.name mpa, mr.id mpa_id,fg.GENRE_ID genre_id, g.name genre_name\n" +
                "FROM films f\n" +
                "INNER JOIN MPA_RATING mr ON f.MPA_ID = mr.ID\n" +
                "INNER JOIN FILM_GENRES fg ON f.id = fg.FILM_ID\n" +
                "INNER JOIN genres g ON fg.genre_id = g.id\n" +
                "ORDER BY genre_id ASC";
        return jdbcTemplate.query(sqlQuery, this::allFilmsRowMapper);
    }

    @Override
    public Film readFilmById(Long id) {
        String sqlQuery = "SELECT f.id film_id, f.name name, f.description description, f.duration duration, f.date_release date_release, mr.name mpa, mr.id mpa_id,fg.GENRE_ID genre_id, g.name genre_name\n" +
                "FROM films f\n" +
                "INNER JOIN MPA_RATING mr ON f.MPA_ID = mr.ID\n" +
                "LEFT OUTER JOIN FILM_GENRES fg ON f.id = fg.FILM_ID\n" +
                "LEFT OUTER JOIN genres g ON fg.genre_id = g.id\n" +
                "WHERE f.id = ?\n" +
                "ORDER BY genre_id ASC";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::filmRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films f \n" +
                "SET f.name = ?, \n" +
                "    f.DESCRIPTION  = ?, \n" +
                "    f.DURATION = ?, \n" +
                "    f.DATE_RELEASE = ?, \n" +
                "    f.MPA_ID =  ?\n" +
                "WHERE f.id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        jdbcTemplate.update("DELETE film_genres where film_genres.film_id = ?", film.getId());
        addFilmGenres(film);
        return film;
    }

    @Override
    public boolean deleteFilm(Long id) {
        String sqlQuery = "delete from films where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    private Film filmRowMapper(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getLong("duration"))
                .releaseDate(rs.getDate("date_release").toLocalDate())
                .genres(new LinkedHashSet<>())
                .build();
        film.setMpa(new MpaRating(rs.getLong("mpa_id"), rs.getString("mpa")));
        do {
            String genreName = rs.getString("genre_name");
            if (genreName != null) {
                Genre genre = new Genre(rs.getLong("genre_id"), genreName);
                film.getGenres().add(genre);
            }
        } while (rs.next());
        return film;
    }

    private List<Film> allFilmsRowMapper(ResultSet rs) throws SQLException {
        Map<Long, Film> films = new HashMap<>();
        while (rs.next()) {
            Long filmId = rs.getLong("id");
            if (!films.containsKey(filmId)) {
                Film film = Film.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .duration(rs.getLong("duration"))
                        .releaseDate(rs.getDate("date_release").toLocalDate())
                        .genres(new LinkedHashSet<>())
                        .build();
                film.setMpa(new MpaRating(rs.getLong("mpa_id"), rs.getString("mpa")));
                films.put(filmId, film);
            }

            Film film = films.get(filmId);
            Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            film.getGenres().add(genre);
        }
        return new ArrayList<>(films.values());
    }

    private void addFilmGenres(Film film) {
        String sqlGenreQuery = "INSERT INTO film_genres (film_id, genre_id)\n" +
                "VALUES (?, ?)";
        for (Genre g : film.getGenres()) {
            jdbcTemplate.update(sqlGenreQuery, film.getId(), g.getId());
        }
    }

}

