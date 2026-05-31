package ru.project.myCinema.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Запрос для обновления данных пользователя")
public record UpdatePersonRequest(
        String login,
        String name,
        String surname
) {
}
