package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class FriendshipRequest {
    private final Integer exporterId;
    private final Integer importerId;
    @EqualsAndHashCode.Exclude
    private Integer statusId;
    @EqualsAndHashCode.Exclude
    private String statusName;

    public FriendshipRequest(int exp, int imp, int statusId) {
        exporterId = exp;
        importerId = imp;
        this.statusId = statusId;
    }
}
