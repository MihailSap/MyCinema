package ru.project.myCinema.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.myCinema.dto.person.PersonResponse;
import ru.project.myCinema.dto.session.SessionResponse;
import ru.project.myCinema.model.BookingStatus;

import java.util.List;

@Schema(title = "Данные заказа")
public record BookingResponse(
        Long id,
        String createdAt,
        BookingStatus status,
        PersonResponse person,
        SessionResponse sessionResponse,
        List<Integer> seatsNumbers
) {
}
