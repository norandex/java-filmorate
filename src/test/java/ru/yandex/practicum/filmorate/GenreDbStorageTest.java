package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGenreById() {
        Genre newGenre = Genre.builder().id(7L).name("Немое кино").build();
        GenreDao genreDao = new GenreDaoImpl(jdbcTemplate);
        genreDao.createGenre(newGenre);

        Genre savedGenre = genreDao.readGenreById(7L);

        Assertions.assertThat(savedGenre).isNotNull().usingRecursiveComparison().isEqualTo(newGenre);
    }

    @Test
    public void testGetAllGenres() {
        GenreDao genreDao = new GenreDaoImpl(jdbcTemplate);

        Genre newGenre = Genre.builder().name("Немое кино").build();
        Genre anotherGenre = Genre.builder().name("Индийское кино").build();

        List<Genre> listGenres = genreDao.readAllGenres();

        genreDao.createGenre(newGenre);
        genreDao.createGenre(anotherGenre);

        listGenres.addAll(List.of(newGenre, anotherGenre));

        List<Genre> readGenreList = genreDao.readAllGenres();

        Assertions.assertThat(listGenres).hasSameElementsAs(readGenreList);
    }
}
