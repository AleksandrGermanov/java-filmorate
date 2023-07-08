package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.validation.IsInSet;
import ru.yandex.practicum.filmorate.validation.Markers;
import ru.yandex.practicum.filmorate.validation.films.AfterCinemaWasBorn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Validated
public class Film {

    @IsInSet(groups = Markers.OnUpdate.class, setHolder = FilmController.class)
    private int id;
    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальный размер - 200 символов.")
    private String description;
    @AfterCinemaWasBorn
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;
}
