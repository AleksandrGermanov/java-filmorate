package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaaRating;

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
public class FilmDBStorage implements FilmStorage {
    private final JdbcTemplate template;

    @Override
    public Film create(Film film) {
        KeyHolder holder = new GeneratedKeyHolder();
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id)"
                + "VALUES(?,?,?,?,?)";
        template.update(con -> {
            PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, holder);
        int filmId = (holder.getKey().intValue());
        for (Genre genre : film.getGenres()) {
            template.update("INSERT INTO films_genres(film_id, genre_id) VALUES (?,?)",
                    filmId, genre.getId());
        }
        return retrieve(filmId);
    }

    @Override
    public Film retrieve(int id) {
        String sql = "SELECT f.*, fg.genre_id, l.user_id, "
                + "r.name AS rating_name, "
                + "g.name AS genre_name "
                + "FROM films AS f "
                + "LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id "
                + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                + "LEFT OUTER JOIN ratings AS r ON f.rating_id = r.id "
                + "LEFT OUTER JOIN genres AS g ON fg.genre_id = g.id "
                + "WHERE f.id = ? ";
        ResultSetExtractor<Film> extractor = (rs) -> {
            Film film = new Film();

            rs.next();
            mapFields(rs, film);
            mapLikes(rs, film);
            mapGenres(rs, film);
            while (rs.next()) {
                mapLikes(rs, film);
                mapGenres(rs, film);
            }
            return film;
        };
        log.info("Proceeding retrieve(" + id + ") operation");
        return template.query(sql, extractor, id);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?,"
                + " duration = ?, rating_id = ?"
                + "WHERE id = ?";
        template.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        template.update("DELETE FROM films_genres WHERE film_id = ?", film.getId());
        for (Genre genre : film.getGenres()) {
            template.update("INSERT INTO films_genres(film_id, genre_id) VALUES (?,?)",
                    film.getId(), genre.getId());
        }
        template.update("DELETE FROM likes WHERE film_id = ?", film.getId());
        for (int userId : film.getLikes()) {
            template.update("INSERT INTO likes (film_id, user_id) VALUES (?,?)",
                    film.getId(), userId);
        }
        return retrieve(film.getId());
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.*, fg.genre_id, l.user_id, "
                + "r.name AS rating_name, "
                + "g.name AS genre_name "
                + "FROM films AS f "
                + "LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id "
                + "LEFT OUTER JOIN likes AS l ON f.id = l.film_id "
                + "LEFT OUTER JOIN ratings AS r ON f.rating_id = r.id "
                + "LEFT OUTER JOIN genres AS g ON fg.genre_id = g.id "
                + "ORDER BY f.name, f.id ";

        ResultSetExtractor<List<Film>> extractor = (rs) -> {
            Film film = new Film();
            film.setId(-1);
            List<Film> list = new ArrayList<>();
            boolean isNotFirst = false;
            while (rs.next()) {
                if (rs.getInt("id") != film.getId()) {
                    if (isNotFirst) {
                        list.add(Film.copyOf(film));
                        film.getGenres().clear();
                        film.getLikes().clear();
                    }
                    isNotFirst = true;
                    mapFields(rs, film);
                }
                mapLikes(rs, film);
                mapGenres(rs, film);
            }
            if (film.getId() != -1) {
                list.add(film);
            }
            log.info("Proceeding findAll() operation");
            return list;
        };
        return template.query(sql, extractor);
    }

    private void mapFields(ResultSet rs, Film film) throws SQLException {
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setMpa(new MpaaRating(rs.getInt("rating_id"),
                rs.getString("rating_name")));
    }

    private void mapLikes(ResultSet rs, Film film) throws SQLException {
        if (rs.getInt("user_id") != 0) {
            film.getLikes().add(rs.getInt("user_id"));
        }
    }

    private void mapGenres(ResultSet rs, Film film) throws SQLException {
        if (rs.getInt("genre_id") != 0) {
            film.getGenres().add(new Genre(rs.getInt("genre_id"),
                    rs.getString("genre_name")));
        }
    }
}

