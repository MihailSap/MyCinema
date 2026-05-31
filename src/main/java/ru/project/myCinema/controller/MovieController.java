package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.movie.MovieRequest;
import ru.project.myCinema.dto.movie.MovieResponse;
import ru.project.myCinema.dto.session.SessionResponseDto;
import ru.project.myCinema.mapper.MovieMapper;
import ru.project.myCinema.mapper.SessionMapper;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.MovieAgeRating;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.service.MovieService;
import ru.project.myCinema.service.SessionService;

import java.util.List;

/**
 * Контроллер для работы с фильмами
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final SessionService sessionService;
    private final SessionMapper sessionMapper;

    @Autowired
    public MovieController(
            MovieService movieService,
            MovieMapper movieMapper,
            SessionService sessionService,
            SessionMapper sessionMapper
    ) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.sessionService = sessionService;
        this.sessionMapper = sessionMapper;
    }

    /**
     * Страница с фильмами
     */
    @GetMapping
    public String getMoviesPage(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        List<MovieResponse> response = movieMapper.mapToMovieResponses(movies);
        model.addAttribute("movies", response);
        return "movies/list";
    }

    /**
     * Страница создания фильма
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createMoviePage(Model model) {
        model.addAttribute("movie", new MovieRequest("", null, 0));
        return "movies/create";
    }

    /**
     * Создание фильма
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createMovie(@ModelAttribute MovieRequest movieRequest) {
        movieService.create(movieRequest);
        return "redirect:/movies";
    }

    /**
     * Страница конкретного фильма
     */
    @GetMapping("/{movieId}")
    public String getMoviePage(@PathVariable("movieId") Long movieId, Model model) {
        Movie movie = movieService.getById(movieId);
        MovieResponse movieResponse = movieMapper.mapToMovieResponse(movie);
        model.addAttribute("movie", movieResponse);

        List<Session> sessions = sessionService.getActualByMovie(movie);
        List<SessionResponseDto> sessionResponseDtos = sessionMapper.mapToSessionResponseDtos(sessions);
        model.addAttribute("sessions", sessionResponseDtos);

        return "movies/details";
    }

    /**
     * Страница редактирования фильма
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{movieId}/edit")
    public String editPage(@PathVariable("movieId") Long movieId, Model model) {
        Movie movie = movieService.getById(movieId);
        MovieRequest request = new MovieRequest(
                movie.getTitle(),
                movie.getAgeRating(),
                movie.getMinutesLength()
        );
        model.addAttribute("movieId", movieId);
        model.addAttribute("movie", request);
        model.addAttribute("ageRatings", MovieAgeRating.values());
        return "movies/edit";
    }

    /**
     * Редактирование фильма
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{movieId}/edit")
    public String updateMovie(
            @PathVariable("movieId") Long movieId,
            @ModelAttribute("movie") MovieRequest movieRequest
    ) {
        Movie movie = movieService.getById(movieId);
        movieService.update(movieRequest, movie);
        return "redirect:/movies/" + movieId;
    }

    /**
     * Удаление фильма
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{movieId}")
    public String delete(@PathVariable("movieId") Long movieId) {
        Movie movie = movieService.getById(movieId);
        movieService.delete(movie);
        return "redirect:/admin";
    }
}