package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Primary
@Slf4j
public class UserDaoImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into users(login, name, email, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public List<User> readAllUsers() {
        String sqlQuery = "select u.id, u.login, u.name, u.email, u.birthday from users u";
        return jdbcTemplate.query(sqlQuery, this::userRowMapper);
    }

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "select id, login, name, email, birthday " +
                "from users where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users u\n" +
                "SET u.LOGIN  = ?, \n" +
                "    u.NAME = ?, \n" +
                "    u.EMAIL = ?, \n" +
                "    u.BIRTHDAY = ?\n" +
                "WHERE u.id = ?;";
        if (jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId()) > 0) return user;
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        String sqlQuery = "delete from users where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    private User userRowMapper(ResultSet rs, int rowNum) throws SQLException {
        log.info(String.valueOf(rowNum));
        return User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

}
