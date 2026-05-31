package ru.project.myCinema.dto.session;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.myCinema.dto.movie.MovieResponse;
import ru.project.myCinema.dto.hall.HallResponse;

@Schema(title = "Данные сеанса")
public record SessionResponse(
        Long id,
        Double ticketPrice,
        String startDateTime,
        HallResponse hall,
        MovieResponse movie
) {
}
