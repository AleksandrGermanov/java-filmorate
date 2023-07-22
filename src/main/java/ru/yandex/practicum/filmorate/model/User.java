package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.IsInSet;
import ru.yandex.practicum.filmorate.validation.Markers;
import ru.yandex.practicum.filmorate.validation.users.NoSpaceSigns;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Validated
public class User {
    @IsInSet(groups = Markers.OnUpdate.class, setHolder = InMemoryUserStorage.class)
    private int id;
    @Email
    private String email;
    @NotBlank
    @NoSpaceSigns
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
