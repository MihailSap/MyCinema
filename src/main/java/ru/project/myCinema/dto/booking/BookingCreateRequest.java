package ru.project.myCinema.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(title = "Запрос на создание заказа")
public record BookingCreateRequest(
        @NotBlank(message = "id сессии должен быть указан")
        Long sessionId,
        @NotBlank(message = "id сессии должен быть указан")
        Long seatId
) {
}
