package ru.project.myCinema.dto.movie;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.myCinema.model.MovieAgeRating;

@Schema(title = "Запрос на создание/редактирование фильма")
public record MovieRequest(
        String title,
        MovieAgeRating ageRating,
        Integer minutesCount
) {
}
