package ru.project.myCinema.dto;

/**
 * Данные пользователя для передачи клиенту
 */
public record PersonResponse(
        Long id,
        String login,
        String name,
        String surname
) {
}
