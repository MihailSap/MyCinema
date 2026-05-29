package ru.project.myCinema.dto;

import java.util.List;

public record HallResponse(
        Long id,
        Integer number,
        Integer capacity,
        List<Integer> seatsNumbers
) {
}
