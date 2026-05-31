package ru.project.myCinema.dto.seat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Данные места в зале")
public record SeatResponse(
        Integer number,
        SeatStatus status
) {
}
