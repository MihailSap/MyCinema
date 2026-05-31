package ru.project.myCinema.dto.session;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Запрос на создание/удаление сеанса")
public record SessionRequest(
        Double ticketPrice,
        String startDateTime,
        Long hallId,
        Long movieId
) {
}
