package ru.project.myCinema.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.project.myCinema.dto.movie.MovieResponse;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.MovieAgeRating;

import java.util.Collections;
import java.util.List;

/**
 * Тесты для маппера фильмов
 */
class MovieMapperTest {

    private MovieMapper movieMapper;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapper();

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("movie");
        movie.setAgeRating(MovieAgeRating.G);
        movie.setMinutesLength(169);
    }

    /**
     * Проверяет маппинг одной сущности Movie в MovieResponse
     */
    @Test
    void testMapToMovieResponse() {
        MovieResponse response = movieMapper.mapToMovieResponse(movie);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(movie.getId(), response.id());
        Assertions.assertEquals(movie.getTitle(), response.title());
        Assertions.assertEquals(movie.getAgeRating(), response.ageRating());
        Assertions.assertEquals(movie.getMinutesLength(), response.minutesLength());
    }

    /**
     * Проверяет маппинг списка фильмов
     */
    @Test
    void testMapToMovieResponses() {
        Movie secondMovie = new Movie();
        secondMovie.setId(2L);
        secondMovie.setTitle("movie2");
        secondMovie.setAgeRating(MovieAgeRating.G);
        secondMovie.setMinutesLength(192);

        List<MovieResponse> responses = movieMapper.mapToMovieResponses(List.of(movie, secondMovie));

        Assertions.assertEquals(2, responses.size());
        Assertions.assertEquals(1L, responses.get(0).id());
        Assertions.assertEquals("movie", responses.get(0).title());
        Assertions.assertEquals(MovieAgeRating.G, responses.get(0).ageRating());
        Assertions.assertEquals(169, responses.get(0).minutesLength());

        Assertions.assertEquals(2L, responses.get(1).id());
        Assertions.assertEquals("movie2", responses.get(1).title());
        Assertions.assertEquals(MovieAgeRating.G, responses.get(1).ageRating());
        Assertions.assertEquals(192, responses.get(1).minutesLength());
    }

    /**
     * Проверяет маппинг пустого списка
     */
    @Test
    void testMapToMovieResponsesEmptyList() {
        List<MovieResponse> responses = movieMapper.mapToMovieResponses(Collections.emptyList());

        Assertions.assertNotNull(responses);
        Assertions.assertTrue(responses.isEmpty());
    }
}