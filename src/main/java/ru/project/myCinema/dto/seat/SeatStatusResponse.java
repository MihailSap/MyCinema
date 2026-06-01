package ru.project.myCinema.dto.seat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Данные места в зале со статусом занятости")
public record SeatStatusResponse(
        Integer number,
        SeatStatus status
) {
}
