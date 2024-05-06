package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.MpaRatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingDao mpaRatingDao;

    public MpaRatingService(MpaRatingDao mpaRatingDao) {
        this.mpaRatingDao = mpaRatingDao;
    }

    public MpaRating getMpaRatingById(Long id) {
        MpaRating mpaRating = mpaRatingDao.readMpaRatingById(id);
        if (mpaRating == null) {
            throw new MpaRatingNotFoundException("Рейтинг не найден");
        }
        return mpaRating;
    }

    public List<MpaRating> readAllMpaRatings() {
        return mpaRatingDao.readAllMpaRatings();
    }
}
