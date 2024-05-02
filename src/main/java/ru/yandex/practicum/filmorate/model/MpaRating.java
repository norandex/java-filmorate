package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MpaRating {
    private Long id;
    private String name;

    public MpaRating(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
