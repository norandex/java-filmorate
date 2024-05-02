package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre createGenre(Genre genre) {
        String sqlQuery = "insert into genres(name) " +
                "values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public List<Genre> readAllGenres() {
        String sqlQuery = "select g.id, g.name, from genres g ORDER BY g.id";
        return jdbcTemplate.query(sqlQuery, this::genreRowMapper);
    }

    @Override
    public Genre readGenreById(Long id) {
        String sqlQuery = "select id, name " +
                "from genres where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::genreRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


    }

    @Override
    public Genre updateGenre(Genre genre) {
        String sqlQuery = "UPDATE genres g\n" +
                "SET g.NAME  = ? \n" +
                "WHERE g.id = ?;";
        jdbcTemplate.update(sqlQuery,
                genre.getName(),
                genre.getId());
        return genre;
    }

    @Override
    public boolean deleteGenre(Long id) {
        String sqlQuery = "delete from genres where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    private Genre genreRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
