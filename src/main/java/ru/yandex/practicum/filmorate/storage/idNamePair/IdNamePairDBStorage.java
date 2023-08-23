package ru.yandex.practicum.filmorate.storage.idNamePair;

import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.IdNamePair;

import java.util.List;


public abstract class IdNamePairDBStorage<T extends IdNamePair> implements IdNamePairStorage<T> {
    private final RowMapper<T> rowMapper = (rs, rn) -> {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        return pairOf(id, name);
    };
    protected String tableName;
    protected JdbcTemplate template;

    public IdNamePairDBStorage(JdbcTemplate template, String tableName) {
        this.template = template;
        this.tableName = tableName;
    }

    @Override
    public List<T> findAll() {
        String sql = String.format("SELECT * FROM %s", tableName);
        log().info("Proceeding findAll() operation");
        return template.query(sql, rowMapper);
    }

    @Override
    public T retrieve(int id) {
        String sql = String.format("SELECT * FROM %s "
                + "WHERE id = ?", tableName);
        log().info("Proceeding retrieve(" + id + ") operation");
        return template.queryForObject(sql, rowMapper, id);
    }

    abstract T pairOf(int id, String name);

    abstract Logger log();
}
