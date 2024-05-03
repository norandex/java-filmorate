package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImpl;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRatingStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testMpaRatingById() {
        MpaRating mpaRating = MpaRating.builder().name("rating1").build();
        MpaRatingDao mpaRatingDao = new MpaRatingDaoImpl(jdbcTemplate);
        mpaRatingDao.createMpaRating(mpaRating);

        MpaRating savedMpa = mpaRatingDao.readMpaRatingById(mpaRating.getId());

        Assertions.assertThat(savedMpa).isNotNull().usingRecursiveComparison().isEqualTo(mpaRating);
    }

    @Test
    public void testGetAllGenres() {
        MpaRatingDao mpaRatingDao = new MpaRatingDaoImpl(jdbcTemplate);

        List<MpaRating> listRatings = mpaRatingDao.readAllMpaRatings();

        MpaRating mpaRating = MpaRating.builder().name("rating1").build();

        mpaRatingDao.createMpaRating(mpaRating);

        MpaRating mpaRatingAnother = MpaRating.builder().name("rating2").build();

        mpaRatingDao.createMpaRating(mpaRatingAnother);


        List<MpaRating> readListRatings = mpaRatingDao.readAllMpaRatings();

        listRatings.addAll(List.of(mpaRating, mpaRatingAnother));


        Assertions.assertThat(listRatings).hasSameElementsAs(readListRatings);
    }
}
