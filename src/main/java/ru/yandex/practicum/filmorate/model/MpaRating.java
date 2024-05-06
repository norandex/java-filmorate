package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class MpaRating {
    private Long id;
    private String name;

    public MpaRating(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
