package ru.project.myCinema.service;

import ru.project.myCinema.model.Booking;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.model.Session;

import java.util.List;

/**
 * Интерфейс для работы с заказами
 */
public interface BookingService {

    /**
     * Получение заказа по id
     */
    Booking getById(Long id);

    /**
     * Получение актуальных неоплаченных заказов пользователя
     */
    List<Booking> getPendingActualByPerson(Person person);

    /**
     * Получение актуальных оплаченных заказов пользователя
     */
    List<Booking> getDoneActualByPerson(Person person);

    /**
     * Оплата заказа
     */
    Booking pay(Booking booking);

    /**
     * Отмена заказа
     */
    Booking cancel(Booking booking);

    /**
     * Создание заказа
     */
    Booking create(Person person, Session session, Seat seat);
}
