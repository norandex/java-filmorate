package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @Past
    @NotNull
    private LocalDate birthday;

    public User(int id, String email, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = login;
    }

    public String getName(){
        if (this.name.isBlank()) {
            return this.getLogin();
        }
        return this.name;
    }
}
