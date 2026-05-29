package ru.project.myCinema.service;

import ru.project.myCinema.dto.MovieRequest;
import ru.project.myCinema.model.Movie;

import java.util.List;

/**
 * Интерфейс для работы с фильмов
 */
public interface MovieService {

    /**
     * Создание фильма
     */
    Movie create(MovieRequest movieRequest);

    /**
     * Получение фильма по id
     */
    Movie getById(Long id);

    /**
     * Получение всех фильмов
     */
    List<Movie> getAllMovies();

    /**
     * Редактирование фильма
     */
    Movie update(MovieRequest movieRequest, Movie movie);

    /**
     * Удаление фильма
     */
    void delete(Movie movie);
}
