package ru.yandex.practicum.filmorate.storage.idNamePair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaaRating;


@Component
public class MpaaRatingDBStorage extends IdNamePairDBStorage<MpaaRating> {
    private static final String tableName = "ratings";

    @Autowired
    public MpaaRatingDBStorage(JdbcTemplate template) {
        super(template, tableName);
    }

    @Override
    protected MpaaRating pairOf(int id, String name) {
        return new MpaaRating(id, name);
    }

    @Override
    protected Logger log() {
        return LoggerFactory.getLogger(this.getClass());
    }
}