package ru.project.myCinema.dto.person;

/**
 * Запрос для обновления данных пользователя
 */
public record UpdatePersonRequest(
        String login,
        String name,
        String surname
) {
}
