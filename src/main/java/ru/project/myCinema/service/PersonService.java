package ru.project.myCinema.service;

import ru.project.myCinema.dto.person.AuthRequest;
import ru.project.myCinema.dto.person.UpdatePersonRequest;
import ru.project.myCinema.model.Person;

import java.util.List;

/**
 * Интерфейс для работы с пользователями
 */
public interface PersonService {

    /**
     * Создание пользователя
     */
    Person create(AuthRequest authRequest);

    /**
     * Получение всех пользователей
     */
    List<Person> getAll();

    /**
     * Получение пользователя по id
     */
    Person getById(Long id);

    /**
     * Получение пользователя по login
     */
    Person getByLogin(String login);

    /**
     * Проверка наличия пользователя с указанным login
     */
    boolean isExistsByLogin(String login);

    /**
     * Обновление данных пользователя
     */
    Person update(UpdatePersonRequest updatePersonRequest, Person person);

    /**
     * Удаление пользователя
     */
    void delete(Person person);

    /**
     * Пополнение баланса пользователя
     */
    Person topUpBalance(Person person, Double amount);

    /**
     * Понижение баланса пользователя
     */
    Person reduceBalance(Person person, Double amount);

    /**
     * Заблокировать пользователя
     */
    Person block(Person person);

    /**
     * Разблокировать пользователя
     */
    Person unblock(Person person);

    /**
     * Смена роли пользователя
     */
    Person changeRole(Long personId);

    /**
     * Получение количества зарегистрированных пользователей
     */
    long getPersonsCount();
}
