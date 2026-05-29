package ru.project.myCinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.Session;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностью сеанса
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Поиск актуальных сеансов
     */
    List<Session> findByStartDateTimeAfter(LocalDateTime dateTime);

    /**
     * Поиск актуальных сеансов по фильму
     */
    List<Session> findByMovieAndStartDateTimeAfter(Movie movie, LocalDateTime dateTime);

    /**
     * Поиск актуальных сеансов по залу
     */
    List<Session> findByHallAndStartDateTimeAfter(Hall hall, LocalDateTime dateTime);
}