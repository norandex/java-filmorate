package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Film.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200, min = 1)
    private String description;
    @Positive
    private long duration;
    private LocalDate releaseDate;
    private MpaRating mpa;
    private Set<Genre> genres = new LinkedHashSet<>();
    private Set<Long> likes = new HashSet<>();

    public Film(long id, String name, String description, long duration, LocalDate releaseDate, MpaRating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
    }
}
