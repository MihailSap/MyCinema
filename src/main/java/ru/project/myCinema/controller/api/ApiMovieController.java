package ru.project.myCinema.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.DefaultResponse;
import ru.project.myCinema.dto.MovieRequest;
import ru.project.myCinema.dto.MovieResponse;
import ru.project.myCinema.mapper.MovieMapper;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.service.MovieService;

import java.util.List;

/**
 * Контроллер для работы с фильмами
 */
@RestController
@RequestMapping("/api/movies")
public class ApiMovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Autowired
    public ApiMovieController(
            MovieService movieService,
            MovieMapper movieMapper
    ) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    /**
     * Получение данных фильма по id
     */
    @GetMapping("/{movieId}")
    public MovieResponse getById(@PathVariable("movieId") Long movieId){
        Movie movie = movieService.getById(movieId);
        return movieMapper.mapToMovieResponse(movie);
    }

    /**
     * Получение всех фильмов
     */
    @GetMapping
    public List<MovieResponse> getAllMovies(){
        List<Movie> movies = movieService.getAllMovies();
        return movieMapper.mapToMovieResponses(movies);
    }

    /**
     * Создание нового фильма
     */
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieResponse create(@RequestBody MovieRequest movieRequest){
        Movie movie = movieService.create(movieRequest);
        return movieMapper.mapToMovieResponse(movie);
    }

    /**
     * Редактирование существующего фильма по id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{movieId}")
    public MovieResponse update(
            @PathVariable("movieId") Long movieId,
            @RequestBody MovieRequest movieRequest
    ){
        Movie movie = movieService.getById(movieId);
        Movie upatedMovie = movieService.update(movieRequest, movie);
        return movieMapper.mapToMovieResponse(upatedMovie);
    }

    /**
     * Удаление фильма по id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{movieId}")
    public DefaultResponse delete(@PathVariable("movieId") Long movieId){
        Movie movie = movieService.getById(movieId);
        movieService.delete(movie);
        return new DefaultResponse("Фильм с id=%s успешно удален".formatted(movie));
    }
}
