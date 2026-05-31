package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.MovieRequest;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.MovieAgeRating;
import ru.project.myCinema.repository.MovieRepository;
import ru.project.myCinema.service.MovieService;

import java.util.List;

/**
 * Реализация интерфейса для работы с фильмом
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    @Override
    public Movie create(MovieRequest movieRequest) {
        Movie movie = new Movie();
        movie.setTitle(movieRequest.title());
        movie.setAgeRating(movieRequest.ageRating());
        movie.setMinutesLength(movieRequest.minutesCount());
        return movieRepository.save(movie);
    }

    @Transactional(readOnly = true)
    @Override
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Фильм с id=%s не найден".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    @Override
    public Movie update(MovieRequest movieRequest, Movie movie) {
        String title = movieRequest.title();
        if(title != null && !title.isEmpty()) {
            movie.setTitle(title);
        }

        MovieAgeRating movieAgeRating = movieRequest.ageRating();
        if(movieAgeRating != null) {
            movie.setAgeRating(movieAgeRating);
        }

        Integer minutesCount = movieRequest.minutesCount();
        if(minutesCount != null) {
            movie.setMinutesLength(minutesCount);
        }
        return movieRepository.save(movie);
    }

    @Transactional
    @Override
    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    @Override
    public long getMovieCount() {
        return movieRepository.count();
    }
}
