package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Film.
 */
@Getter
@Setter
@ToString
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200, min = 1)
    private String description;
    @Positive
    private long duration;
    private LocalDate releaseDate;
    private Set<Long> likes = new HashSet<>();


}
