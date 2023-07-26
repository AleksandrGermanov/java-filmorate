package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.service.Visitor;
import ru.yandex.practicum.filmorate.service.validation.users.NoSpaceSigns;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Validated
public class User {
    @Getter
    private final Set<Integer> friends = new HashSet<>();
    private int id;
    @Email
    private String email;
    @NotBlank
    @NoSpaceSigns
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public void accept(Visitor<User> v) {
        v.visit(this);
    }
}
