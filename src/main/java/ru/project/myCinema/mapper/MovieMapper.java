package ru.project.myCinema.mapper;

import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.movie.MovieResponse;
import ru.project.myCinema.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер сущности фильма
 */
@Component
public class MovieMapper {

    /**
     * Маппинг сущностей Movie в MovieResponse
     */
    public List<MovieResponse> mapToMovieResponses(List<Movie> movies) {
        List<MovieResponse> movieResponses = new ArrayList<>();
        for (Movie movie : movies) {
            movieResponses.add(mapToMovieResponse(movie));
        }
        return movieResponses;
    }

    /**
     * Маппинг сущности Movie в MovieResponse
     */
    public MovieResponse mapToMovieResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getAgeRating(),
                movie.getMinutesLength()
        );
    }
}
