package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
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
    private Set<Long> friends = new HashSet<>();

    public User(Long id, String email, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = login;
    }

    public User(Long id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = name;
    }

    public String getName() {
        if (this.name == null || this.name.isBlank()) {
            this.name = this.getLogin();
        }
        return this.name;
    }

}
