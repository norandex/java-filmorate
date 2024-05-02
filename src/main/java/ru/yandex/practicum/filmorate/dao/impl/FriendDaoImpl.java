package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FriendDaoImpl implements FriendDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userDao;


    public FriendDaoImpl(JdbcTemplate jdbcTemplate, UserStorage userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }


    @Override
    public List<User> getFriendsOfUser(Long id) {
        String sqlQuery = "select f.USER_ID user_id, f.friend_id friend_id, s.status status\n" +
                "from friends f\n" +
                "inner join statuses s ON f.status_id = s.id\n" +
                "where user_id = ?";
        try {
            List<Friend> friends = jdbcTemplate.query(sqlQuery, this::friendsMapper, id);
            if (friends.isEmpty()) {
                return Collections.emptyList();
            }
            return friends.stream()
                    .map(Friend::getFriendId)
                    .map(userDao::getUserById)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> getRequestsOfUser(Long id) {
        String sqlQuery = "select f.USER_ID user_id, f.friend_id friend_id, s.status status\n" +
                "from friends f\n" +
                "inner join statuses s ON f.status_id = s.id\n" +
                "where user_id = ? AND status = 'requested'";
        List<Friend> friends = jdbcTemplate.query(sqlQuery, this::friendsMapper, id);
        if (friends.isEmpty()) {
            return Collections.emptyList();
        }
        return friends.stream()
                .map(Friend::getFriendId)
                .map(userDao::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User deleteFriends(Long userId, Long friendId) {
        String sqlQuery = "DELETE friends WHERE friends.USER_ID = ? AND friends.FRIEND_ID = ?;";
        if (jdbcTemplate.update(sqlQuery, userId, friendId) > 0) {
            setFriendRequestStatus(userId, friendId, "requested");
        }
        return userDao.getUserById(userId);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        String status = "requested";
        if (checkIfThereIsACounterRequest(userId, friendId)) {
            status = "accepted";
            setFriendRequestStatus(friendId, userId, status);
        }
        String sqlQuery = "INSERT INTO friends (USER_ID, FRIEND_ID, STATUS_ID)\n" +
                "VALUES (?,?,SELECT statuses.id FROM statuses WHERE statuses.STATUS = ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId, status);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return userDao.getUserById(userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        String sqlQuery = "SELECT f.friend_id\n" +
                "FROM friends f\n" +
                "INNER JOIN friends f2 ON f.friend_id = f2.FRIEND_ID \n" +
                "WHERE f.user_id = ? AND f2.USER_ID = ?";
        List<Long> friendsIds = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("friend_id"), userId, friendId);
        if (friendsIds.isEmpty()) {
            return Collections.emptyList();
        }
        return friendsIds.stream()
                .map(userDao::getUserById)
                .collect(Collectors.toList());
    }

    private boolean checkIfThereIsACounterRequest(Long userId, Long friendId) {
        List<User> friendsOfFriend = getRequestsOfUser(friendId);
        if (friendsOfFriend.isEmpty()) return false;
        return (friendsOfFriend.stream().map(User::getId).collect(Collectors.toList()).contains(userId));
    }

    private void setFriendRequestStatus(Long userId, Long friendId, String status) {
        String sqlQuery = "UPDATE friends \n" +
                "SET friends.status_id = SELECT statuses.id FROM statuses WHERE statuses.status = ?\n" +
                "WHERE friends.USER_ID = ? AND friends.friend_id = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId, status);
    }

    private List<Friend> friendsMapper(ResultSet rs) throws SQLException {
        List<Friend> friends = new ArrayList<>();
        while (rs.next()) {
            Friend friend = new Friend(rs.getLong("user_id"), rs.getLong("friend_id"), rs.getString("status"));
            friends.add(friend);
        }
        return friends;
    }
}
