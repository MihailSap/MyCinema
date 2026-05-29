package ru.project.myCinema.dto;

import java.util.List;

public record HallRequest(
        Integer number,
        Integer capacity,
        List<Integer> seatsNumbers
) {
}
