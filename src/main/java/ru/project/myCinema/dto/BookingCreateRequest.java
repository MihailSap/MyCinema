package ru.project.myCinema.dto;

public record BookingCreateRequest(
        Long sessionId,
        Long seatId
) {
}
