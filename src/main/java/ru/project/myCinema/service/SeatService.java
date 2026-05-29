package ru.project.myCinema.service;

import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.model.Session;

import java.util.List;

/**
 * Интерфейс для работы с местом
 */
public interface SeatService {

    /**
     * Получение места по id
     */
    Seat getById(Long id);

    /**
     * Проверка места на бронь
     */
    boolean isSeatBooked(Seat seat, Session session);

    /**
     * Создание мест для зала
     */
    void createByHall(List<Integer> seatsNumbers, Hall hall);

    /**
     * Получение мест в зале
     */
    List<Seat> getSeatsByHall(Hall hall);

    /**
     * Удаление мест в зале
     */
    void deleteByHall(Hall hall);
}
