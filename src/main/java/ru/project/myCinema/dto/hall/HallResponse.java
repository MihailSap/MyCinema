package ru.project.myCinema.dto.hall;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(title = "Данные зала")
public record HallResponse(
        Long id,
        Integer number,
        Integer capacity,
        List<Integer> seatsNumbers
) {
}
