package ru.project.myCinema.dto;

/**
 * Запрос на регистрацию/вход
 */
public record AuthRequest(String login, String password) {
}
