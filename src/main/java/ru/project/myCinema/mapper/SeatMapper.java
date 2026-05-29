package ru.project.myCinema.mapper;

import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.SeatResponse;
import ru.project.myCinema.dto.SeatStatus;
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
     * Маппинг сущности Seat в SeatResponse
     */
    public SeatResponse mapToSeatResponse(Seat seat, boolean isBooked) {
        return new SeatResponse(
                seat.getNumber(),
                isBooked ? SeatStatus.TAKEN : SeatStatus.AVAILABLE
        );
    }
}
