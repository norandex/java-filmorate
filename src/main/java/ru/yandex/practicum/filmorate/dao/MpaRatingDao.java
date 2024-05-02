package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingDao {
    MpaRating createMpaRating(MpaRating mpaRating);

    List<MpaRating> readAllMpaRatings();

    MpaRating readMpaRatingById(Long id);

    MpaRating updateMpaRating(MpaRating mpaRating);

    boolean deleteMpaRating(Long id);
}
