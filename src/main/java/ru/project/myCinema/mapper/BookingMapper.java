package ru.project.myCinema.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.booking.BookingResponse;
import ru.project.myCinema.model.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер заказов
 */
@Component
public class BookingMapper {

    private final PersonMapper personMapper;
    private final SessionMapper sessionMapper;
    private final SeatMapper seatMapper;

    @Autowired
    public BookingMapper(
            PersonMapper personMapper,
            SessionMapper sessionMapper,
            SeatMapper seatMapper
    ) {
        this.personMapper = personMapper;
        this.sessionMapper = sessionMapper;
        this.seatMapper = seatMapper;
    }

    /**
     * Маппинг сущностей Booking в BookingResponse
     */
    public List<BookingResponse> mapToBookingResponses(List<Booking> bookings) {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponses.add(mapToBookingResponse(booking));
        }
        return bookingResponses;
    }

    /**
     * Маппинг сущности Booking в BookingResponse
     */
    public BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getCreatedAt().toString(),
                booking.getStatus(),
                personMapper.mapToPersonResponse(booking.getPerson()),
                sessionMapper.mapToSessionResponse(booking.getSession()),
                seatMapper.mapToSeatsNumbers(booking.getSeats())
        );
    }
}
