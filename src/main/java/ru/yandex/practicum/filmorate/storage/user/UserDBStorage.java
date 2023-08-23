package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipRequest;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate template;

    @Override
    public User create(User user) {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (name, email, login, birthday)"
                + "VALUES(?,?,?,?)";
        template.update(con -> {
            PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, holder);
        log.info("Create operation to user with id " + user.getId() + "has been proceeded.");
        return retrieve(holder.getKey().intValue());
    }

    @Override
    public User retrieve(int id) {
        String sql = "SELECT u.*, "
                + "req.request_exporter_id AS request_from, "
                + "req.request_importer_id AS request_to, "
                + "req.friendship_status_id AS status_id, "
                + "s.name AS status_name "
                + "FROM users AS u "
                + "LEFT OUTER JOIN friendship_and_requests AS req ON (u.id = req.request_exporter_id "
                + "OR u.id = req.request_importer_id) "
                + "LEFT OUTER JOIN friendship_statuses AS s ON req.friendship_status_id = s.id "
                + "WHERE u.id = ?";
        ResultSetExtractor<User> extractor = (rs) -> {
            User user = new User();
            rs.next();
            mapFieldsPart(user, rs);
            mapRequestPart(user, rs);
            while (rs.next()) {
                mapRequestPart(user, rs);
            }
            return user;
        };
        log.info("Proceeding retrieve(" + id + ") operation");
        return template.query(sql, extractor, id);
    }

    @Override
    public User update(User user) {
        String sqlUpdateUsers = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ?" +
                "WHERE id = ?";
        template.update(sqlUpdateUsers, user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday(), user.getId());
        for (FriendshipRequest request : user.getRequests()) {
            template.update("MERGE INTO friendship_and_requests (request_exporter_id, "
                            + "request_importer_id, friendship_status_id) VALUES (?,?,?)", request.getExporterId(),
                    request.getImporterId(), request.getStatusId());
        }
        log.info("Proceeding update operation to user with id " + user.getId() + ".");
        return retrieve(user.getId());
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT u.*, "
                + "req.request_exporter_id AS request_from, "
                + "req.request_importer_id AS request_to, "
                + "req.friendship_status_id AS status_id, "
                + "s.name AS status_name "
                + "FROM users AS u "
                + "LEFT OUTER JOIN friendship_and_requests AS req ON (u.id = req.request_exporter_id "
                + "OR u.id = req.request_importer_id) "
                + "LEFT OUTER JOIN friendship_statuses AS s ON req.friendship_status_id = s.id "
                + "ORDER BY u.id";
        ResultSetExtractor<List<User>> extractor = (rs) -> {
            User user = new User();
            user.setId(-1);
            List<User> list = new ArrayList<>();
            boolean isNotFirst = false;
            while (rs.next()) {
                if (rs.getInt("id") != user.getId()) {
                    if (isNotFirst) {
                        list.add(User.copyOf(user));
                        user.getRequests().clear();
                        user.getFriends().clear();
                    }
                    isNotFirst = true;
                    mapFieldsPart(user, rs);
                }
                mapRequestPart(user, rs);
            }
            if (user.getId() != -1) {
                list.add(user);
            }
            return list;
        };
        log.info("Proceeding findAll() operation");
        return template.query(sql, extractor);
    }

    private void mapRequestPart(User user, ResultSet rs) throws SQLException {
        if (rs.getInt("request_to") != 0
                && rs.getInt("request_from") != 0) {
            FriendshipRequest request = new FriendshipRequest(rs.getInt("request_from"),
                    rs.getInt("request_to"),
                    rs.getInt("status_id"),
                    rs.getString("status_name"));
            user.getRequests().add(request);
            if (request.getStatusId() == 2 && request.getImporterId() == user.getId()) {
                user.getFriends().add(request.getExporterId());
            }
        }
    }


    private void mapFieldsPart(User user, ResultSet rs) throws SQLException {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
    }
}
