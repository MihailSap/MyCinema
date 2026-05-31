package ru.project.myCinema.dto.hall;

import java.util.List;

public record HallResponse(
        Long id,
        Integer number,
        Integer capacity,
        List<Integer> seatsNumbers
) {
}
