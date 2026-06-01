package ru.project.myCinema.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "Запрос на регистрацию/вход")
public record AuthRequest(
        @Size(min = 4, max = 30, message = "Логин пользователя должен иметь длину от 4 до 30 символов")
        @NotBlank(message = "Логин пользователя должен быть заполнен")
        String login,
        @Size(min = 8, message = "Пароль пользователя должен иметь длину от 8 символов")
        @NotBlank(message = "Пароль пользователя должен быть заполнен")
        String password
) {
}
