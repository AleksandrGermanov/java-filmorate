package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.service.validation.films.AfterCinemaWasBorn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Validated
public class Film implements Comparable<Film> {
    @Getter
    private final Set<Integer> likes = new HashSet<>();
    private int id;
    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальный размер - 200 символов.")
    private String description;
    @AfterCinemaWasBorn
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;

    @Override
    public int compareTo(Film o) {
        return Integer.compare(likes.size(), o.likes.size());
    }

    public void accept(Visitor<Film> v) {
        v.visit(this);
    }
}
