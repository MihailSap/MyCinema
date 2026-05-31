package ru.project.myCinema.dto.seat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Данные места в зале с id")
public record SeatShortResponse(
        Long id,
        Integer number
) {
}
