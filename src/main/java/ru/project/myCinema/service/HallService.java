package ru.project.myCinema.service;

import ru.project.myCinema.dto.hall.HallRequest;
import ru.project.myCinema.model.Hall;

import java.util.List;

/**
 * Интерфейс для работы с залом
 */
public interface HallService {

    /**
     * Создание зала
     */
    Hall create(HallRequest hallRequest);

    /**
     * Получение зала по id
     */
    Hall getById(Long id);

    /**
     * Получение всех залов
     */
    List<Hall> getAllHalls();

    /**
     * Редактирование зала
     */
    Hall update(HallRequest hallRequest, Hall hall);

    /**
     * Удаление зала
     */
    void delete(Hall hall);
}
