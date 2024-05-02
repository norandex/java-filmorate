package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@Slf4j
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping("/mpa")
    public List<MpaRating> findAll() {
        log.info("MpaRating > Get All Request");
        return mpaRatingService.readAllMpaRatings();
    }

    @GetMapping("/mpa/{ratingId}")
    public MpaRating findById(@PathVariable("ratingId") Long ratingId) {
        return mpaRatingService.getMpaRatingById(ratingId);
    }
}
