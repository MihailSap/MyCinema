package ru.project.myCinema.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Запрос на создание заказа")
public record BookingCreateRequest(
        Long sessionId,
        Long seatId
) {
}
