package ru.project.myCinema.dto;

public record SessionRequest(
        Double ticketPrice,
        String startDateTime,
        Long hallId,
        Long movieId
) {
}
