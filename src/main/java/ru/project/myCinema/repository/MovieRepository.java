package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Movie;

/**
 * Репозиторий для работы с сущностью фильма
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
