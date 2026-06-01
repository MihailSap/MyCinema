package ru.project.myCinema.mapper;

import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.seat.SeatStatusResponse;
import ru.project.myCinema.dto.seat.SeatIdResponse;
import ru.project.myCinema.dto.seat.SeatStatus;
import ru.project.myCinema.model.Seat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Маппер сущности места
 */
@Component
public class SeatMapper {

    /**
     * Маппинг сущностей Seat в список номеров
     */
    public List<Integer> mapToSeatsNumbers(List<Seat> seats) {
        List<Integer> seatsNumbers = new ArrayList<>();
        for (Seat seat : seats) {
            seatsNumbers.add(seat.getNumber());
        }
        return seatsNumbers;
    }

    /**
     * Маппинг сущностей Seat в список номеров
     */
    public List<Integer> mapToSeatsNumbers(Set<Seat> seats) {
        List<Integer> seatsNumbers = new ArrayList<>();
        for (Seat seat : seats) {
            seatsNumbers.add(seat.getNumber());
        }
        return seatsNumbers;
    }

    /**
     * Маппинг сущности Seat в SeatStatusResponse
     */
    public SeatStatusResponse mapToSeatStatusResponse(Seat seat, boolean isBooked) {
        return new SeatStatusResponse(
                seat.getNumber(),
                isBooked ? SeatStatus.TAKEN : SeatStatus.AVAILABLE
        );
    }

    /**
     * Маппинг сущностей Seat в SeatIdResponse
     */
    public List<SeatIdResponse> mapToSeatIdResponses(List<Seat> seats) {
        List<SeatIdResponse> seatIdResponses = new ArrayList<>();
        for (Seat seat : seats) {
            seatIdResponses.add(mapToSeatIdResponse(seat));
        }
        return seatIdResponses;
    }

    /**
     * Маппинг сущности Seat в SeatIdResponse
     */
    private SeatIdResponse mapToSeatIdResponse(Seat seat){
        return new SeatIdResponse(
                seat.getId(),
                seat.getNumber()
        );
    }
}
