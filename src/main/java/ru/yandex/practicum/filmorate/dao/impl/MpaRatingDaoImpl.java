package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaRatingDaoImpl implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating createMpaRating(MpaRating mpaRating) {
        String sqlQuery = "insert into mpa_rating(name) " +
                "values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, mpaRating.getName());
            return stmt;
        }, keyHolder);
        mpaRating.setId(keyHolder.getKey().longValue());
        return mpaRating;
    }

    @Override
    public List<MpaRating> readAllMpaRatings() {
        String sqlQuery = "select mp.id, mp.name, from mpa_rating mp ORDER BY mp.id";
        return jdbcTemplate.query(sqlQuery, this::MpaRatingRowMapper);
    }

    @Override
    public MpaRating readMpaRatingById(Long id) {
        String sqlQuery = "select id, name " +
                "from mpa_rating where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::MpaRatingRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public MpaRating updateMpaRating(MpaRating mpaRating) {
        String sqlQuery = "UPDATE mpa_rating g\n" +
                "SET g.NAME  = ? \n" +
                "WHERE g.id = ?;";
        jdbcTemplate.update(sqlQuery,
                mpaRating.getName(),
                mpaRating.getId());
        return mpaRating;
    }

    @Override
    public boolean deleteMpaRating(Long id) {
        String sqlQuery = "delete from mpa_rating where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    private MpaRating MpaRatingRowMapper(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

}
