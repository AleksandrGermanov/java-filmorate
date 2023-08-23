package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.service.validation.films.AfterCinemaWasBorn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Validated
public class Film implements Comparable<Film> {
    @EqualsAndHashCode.Exclude
    private final Set<Integer> likes = new HashSet<>();
    @EqualsAndHashCode.Exclude
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    private Integer id;
    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальный размер - 200 символов.")
    private String description;
    @AfterCinemaWasBorn
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;
    private MpaaRating mpa;

    public static Film copyOf(Film film) {
        Film copy = new Film();
        copy.id = film.id;
        copy.name = film.name;
        copy.description = film.description;
        copy.duration = film.duration;
        copy.releaseDate = film.releaseDate;
        copy.mpa = film.mpa;
        copy.likes.addAll(film.likes);
        copy.genres.addAll(film.genres);
        return copy;
    }

    @Override
    public int compareTo(Film o) {
        return Integer.compare(likes.size(), o.likes.size());
    }

    public void accept(Visitor<Film> v) {
        v.visit(this);
    }
}
