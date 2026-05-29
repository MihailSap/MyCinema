package ru.project.myCinema.dto;

import ru.project.myCinema.model.BookingStatus;

import java.util.List;

public record BookingResponse(
        String createdAt,
        BookingStatus status,
        PersonResponse person,
        SessionResponse sessionResponse,
        List<Integer> seatsNumbers
) {
}
