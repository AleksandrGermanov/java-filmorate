package ru.yandex.practicum.filmorate.storage.idNamePair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
public class GenreDBStorage extends IdNamePairDBStorage<Genre> {
    private static final String tableName = "genres";

    @Autowired
    public GenreDBStorage(JdbcTemplate template) {
        super(template, tableName);
    }

    @Override
    Genre pairOf(int id, String name) {
        return new Genre(id, name);
    }

    @Override
    Logger log() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
