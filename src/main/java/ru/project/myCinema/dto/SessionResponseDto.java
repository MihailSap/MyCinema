package ru.project.myCinema.dto;

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
