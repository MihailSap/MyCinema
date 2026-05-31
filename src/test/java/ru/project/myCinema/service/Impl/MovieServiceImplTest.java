package ru.project.myCinema.service.Impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.movie.MovieRequest;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.MovieAgeRating;
import ru.project.myCinema.repository.MovieRepository;
import ru.project.myCinema.service.MovieService;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MovieServiceImplTest {

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;

    /**
     * Проверка создания фильма
     */
    @Test
    void testCreateMovie() {
        MovieRequest movieRequest = new MovieRequest(
                "movie",
                MovieAgeRating.PG13,
                120
        );

        Movie createdMovie = movieService.create(movieRequest);
        Assertions.assertNotNull(createdMovie);
        Assertions.assertEquals("movie", createdMovie.getTitle());
        Assertions.assertEquals(MovieAgeRating.PG13, createdMovie.getAgeRating());
        Assertions.assertEquals(120, createdMovie.getMinutesLength());
    }

    /**
     * Проверка получения фильма по id
     */
    @Test
    void testGetByIdSuccess() {
        Movie movie = movieService.create(new MovieRequest("movie", MovieAgeRating.G, 90));

        Movie foundMovie = movieService.getById(movie.getId());

        Assertions.assertNotNull(foundMovie);
        Assertions.assertEquals(movie, foundMovie);
    }

    /**
     * Проверка получения несуществующего фильма
     */
    @Test
    void testGetByIdFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> movieService.getById(999L));
    }

    /**
     * Проверка получения всех фильмов
     */
    @Test
    void testGetAllMovies() {
        movieService.create(new MovieRequest("movie1", MovieAgeRating.G, 90));
        movieService.create(new MovieRequest("movie2", MovieAgeRating.PG, 120));

        List<Movie> movies = movieService.getAllMovies();
        Assertions.assertNotNull(movies);
        Assertions.assertEquals(2, movies.size());
        Assertions.assertEquals("movie1", movies.getFirst().getTitle());
        Assertions.assertEquals("movie2", movies.get(1).getTitle());
    }

    /**
     * Проверка полного обновления фильма
     */
    @Test
    void testUpdateMovie() {
        Movie movie = movieService.create(new MovieRequest("oldMovie", MovieAgeRating.G, 90));
        MovieRequest updateRequest = new MovieRequest("newMovie", MovieAgeRating.R, 150);
        Movie updatedMovie = movieService.update(updateRequest, movie);

        Assertions.assertNotNull(updatedMovie);
        Assertions.assertEquals("newMovie", updatedMovie.getTitle());
        Assertions.assertEquals(MovieAgeRating.R, updatedMovie.getAgeRating());
        Assertions.assertEquals(150, updatedMovie.getMinutesLength());
    }

    /**
     * Проверка частичного обновления фильма
     */
    @Test
    void testUpdatePartMovie() {
        Movie movie = movieService.create(new MovieRequest("movie", MovieAgeRating.PG13, 169));
        MovieRequest updateRequest = new MovieRequest(null, null, 180);
        Movie updatedMovie = movieService.update(updateRequest, movie);

        Assertions.assertEquals("movie", updatedMovie.getTitle());
        Assertions.assertEquals(MovieAgeRating.PG13, updatedMovie.getAgeRating());
        Assertions.assertEquals(180, updatedMovie.getMinutesLength());
    }

    /**
     * Проверка удаления фильма
     */
    @Test
    void testDeleteMovie() {
        Movie movie = movieService.create(new MovieRequest("movie", MovieAgeRating.G, 90));
        movieService.delete(movie);
        Assertions.assertFalse(movieRepository.findById(movie.getId()).isPresent());
    }

    /**
     * Проверка получения количества фильмов
     */
    @Test
    void testGetMovieCount() {
        movieService.create(new MovieRequest("movie1", MovieAgeRating.G, 90));
        movieService.create(new MovieRequest("movie2", MovieAgeRating.PG, 120));

        long movieCount = movieService.getMovieCount();

        Assertions.assertEquals(2, movieCount);
    }
}