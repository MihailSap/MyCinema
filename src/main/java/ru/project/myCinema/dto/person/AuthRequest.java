package ru.project.myCinema.dto.person;

/**
 * Запрос на регистрацию/вход
 */
public record AuthRequest(
        String login,
        String password
) {
}
