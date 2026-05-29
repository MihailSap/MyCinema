package ru.project.myCinema.dto;

public record SessionResponse(
        Long id,
        Double ticketPrice,
        String startDateTime,
        HallResponse hall,
        MovieResponse movie
) {
}
