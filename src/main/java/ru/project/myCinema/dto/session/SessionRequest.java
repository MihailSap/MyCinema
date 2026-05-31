package ru.project.myCinema.dto.session;

public record SessionRequest(
        Double ticketPrice,
        String startDateTime,
        Long hallId,
        Long movieId
) {
}
