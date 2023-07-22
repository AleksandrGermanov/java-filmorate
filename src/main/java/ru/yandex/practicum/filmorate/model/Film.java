package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.service.FilmVisitor;
import ru.yandex.practicum.filmorate.service.validation.IsInSet;
import ru.yandex.practicum.filmorate.service.validation.Markers;
import ru.yandex.practicum.filmorate.service.validation.films.AfterCinemaWasBorn;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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
    @IsInSet(groups = Markers.OnUpdate.class, setHolder = InMemoryFilmStorage.class)
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
        if (likes.size() > o.likes.size()) {
            return 1;
        }
        if (likes.size() < o.likes.size()) {
            return -1;
        }
        return 0;
    }

    public void accept(FilmVisitor v) {
        v.visit(this);
    }
}
