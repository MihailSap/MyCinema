package ru.project.myCinema.dto.hall;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Запрос на создание/редактирование зала")
public record HallRequest(
        Integer number,
        Integer capacity
) {
}
