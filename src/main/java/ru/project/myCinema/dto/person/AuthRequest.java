package ru.project.myCinema.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Запрос на регистрацию/вход")
public record AuthRequest(
        String login,
        String password
) {
}
