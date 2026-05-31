package ru.project.myCinema.service;

import ru.project.myCinema.dto.session.SessionRequest;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.Session;

import java.util.List;

/**
 * Интерфейс для работы с сеансом
 */
public interface SessionService {

    /**
     * Получение сеанса по id
     */
    Session getById(Long id);

    /**
     * Получение всех сеансов
     */
    List<Session> getAllSessions();

    /**
     * Получение всех предстоящих сеансов
     */
    List<Session> getActualSessions();

    /**
     * Получение всех предстоящих сеансов фильма
     */
    List<Session> getActualByMovie(Movie movie);

    /**
     * Получение всех предстоящих сеансов в зале
     */
    List<Session> getActualByHall(Hall hall);

    /**
     * Создание сеанса
     */
    Session create(SessionRequest sessionRequest, Hall hall, Movie movie);

    /**
     * Редактирование данных сеанса
     */
    Session update(SessionRequest sessionRequest, Session session);

    /**
     * Редактирование зала сеанса
     */
    Session updateHall(Session session, Hall hall);

    /**
     * Редактирование фильма сеанса
     */
    Session updateMovie(Session session, Movie movie);

    /**
     * Удаление сеанса
     */
    void delete(Session session);
}
