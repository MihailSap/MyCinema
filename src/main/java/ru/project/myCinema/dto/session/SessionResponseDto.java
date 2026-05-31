package ru.project.myCinema.dto.session;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Данные актуального сеанса")
public record SessionResponseDto(
        Long id,
        Double ticketPrice,
        String startDateTime,
        Long hallId,
        Integer hallNumber,
        Long movieId,
        String movieTitle
) {
}
