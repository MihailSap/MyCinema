package ru.project.myCinema.dto;

/**
 * Запрос для обновления данных пользователя
 */
public record UpdatePersonRequest(
        String login,
        String name,
        String surname
) {
}
