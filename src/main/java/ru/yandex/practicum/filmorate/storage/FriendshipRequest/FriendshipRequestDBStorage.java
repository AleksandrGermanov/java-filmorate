package ru.yandex.practicum.filmorate.storage.FriendshipRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipRequest;

@Component
@Data
@Slf4j
public class FriendshipRequestDBStorage implements FriendshipRequestStorage {
    private final JdbcTemplate template;

    @Override
    public FriendshipRequest upsert(FriendshipRequest request) {
        template.update("MERGE INTO friendship_and_requests (request_exporter_id, "
                        + "request_importer_id, friendship_status_id) VALUES (?,?,?)", request.getExporterId(),
                request.getImporterId(), request.getStatusId());
        log.info("Upsert operation with parameters " + request.getExporterId()
                + ", " + request.getImporterId() + ", " + request.getStatusId() + " has been proceed.");
        return request;
    }
}
