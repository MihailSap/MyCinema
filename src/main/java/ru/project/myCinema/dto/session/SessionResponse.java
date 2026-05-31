package ru.project.myCinema.dto.session;

import ru.project.myCinema.dto.movie.MovieResponse;
import ru.project.myCinema.dto.hall.HallResponse;

public record SessionResponse(
        Long id,
        Double ticketPrice,
        String startDateTime,
        HallResponse hall,
        MovieResponse movie
) {
}
