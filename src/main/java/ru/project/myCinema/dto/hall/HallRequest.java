package ru.project.myCinema.dto.hall;

import java.util.List;

public record HallRequest(
        Integer number,
        Integer capacity,
        List<Integer> seatsNumbers
) {
}
