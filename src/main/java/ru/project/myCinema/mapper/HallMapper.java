package ru.project.myCinema.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.myCinema.dto.HallResponse;
import ru.project.myCinema.model.Hall;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер сущности зала
 */
@Component
public class HallMapper {

    private final SeatMapper seatMapper;

    @Autowired
    public HallMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    /**
     * Маппинг сущностей Hall в HallResponse
     */
    public List<HallResponse> mapToHallResponses(List<Hall> halls) {
        List<HallResponse> hallResponses = new ArrayList<>();
        for (Hall hall : halls) {
            hallResponses.add(mapToHallResponse(hall));
        }
        return hallResponses;
    }

    /**
     * Маппинг сущности Hall в HallResponse
     */
    public HallResponse mapToHallResponse(Hall hall) {
        return new HallResponse(
                hall.getId(),
                hall.getNumber(),
                hall.getCapacity(),
                seatMapper.mapToSeatsNumbers(hall.getSeats())
        );
    }
}
