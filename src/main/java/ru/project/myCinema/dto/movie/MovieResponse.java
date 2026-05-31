package ru.project.myCinema.dto.movie;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.myCinema.model.MovieAgeRating;

@Schema(title = "Данные фильма")
public record MovieResponse(
        Long id,
        String title,
        MovieAgeRating ageRating,
        Integer minutesLength
) {
}
