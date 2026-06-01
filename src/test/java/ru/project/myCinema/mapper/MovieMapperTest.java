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

    private final MovieMapper movieMapper = new MovieMapper();
    private Movie movie;

    @BeforeEach
    void setUp() {
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
        secondMovie.setAgeRating(MovieAgeRating.PG);
        secondMovie.setMinutesLength(192);

        List<MovieResponse> responses = movieMapper.mapToMovieResponses(List.of(movie, secondMovie));

        Assertions.assertEquals(2, responses.size());

        MovieResponse firstMovieResponse = responses.getFirst();
        Assertions.assertNotNull(firstMovieResponse);
        Assertions.assertEquals(1L, firstMovieResponse.id());
        Assertions.assertEquals("movie", firstMovieResponse.title());
        Assertions.assertEquals(MovieAgeRating.G, firstMovieResponse.ageRating());
        Assertions.assertEquals(169, firstMovieResponse.minutesLength());

        MovieResponse secondMovieResponse = responses.get(1);
        Assertions.assertNotNull(secondMovieResponse);
        Assertions.assertEquals(2L, secondMovieResponse.id());
        Assertions.assertEquals("movie2", secondMovieResponse.title());
        Assertions.assertEquals(MovieAgeRating.PG, secondMovieResponse.ageRating());
        Assertions.assertEquals(192, secondMovieResponse.minutesLength());
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