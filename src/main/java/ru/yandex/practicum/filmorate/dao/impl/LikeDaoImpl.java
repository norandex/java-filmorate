package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userDao;
    private final FilmStorage filmDao;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate, UserStorage userDao, FilmStorage filmDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.filmDao = filmDao;
    }

    @Override
    public Film likeFilm(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id)\n" +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        return filmDao.readFilmById(filmId);
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE likes WHERE user_id = ? AND film_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, filmId) > 0;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.id id, COUNT(f.id) likes_total\n" +
                "FROM FILMS f\n" +
                "INNER JOIN likes l ON l.film_id = f.id\n" +
                "INNER JOIN MPA_RATING mr ON f.MPA_ID = mr.ID\n" +
                "GROUP BY id\n" +
                "ORDER BY likes_total DESC\n" +
                "LIMIT ?;";
        return jdbcTemplate.query(sqlQuery, this::FilmsRowMapper, count);
    }

    private List<Film> FilmsRowMapper(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        while (rs.next()) {
            films.add(filmDao.readFilmById(rs.getLong("id")));
        }
        return films;
    }
}
