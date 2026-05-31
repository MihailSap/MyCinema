package ru.project.myCinema.dto.booking;

public record BookingCreateRequest(
        Long sessionId,
        Long seatId
) {
}
