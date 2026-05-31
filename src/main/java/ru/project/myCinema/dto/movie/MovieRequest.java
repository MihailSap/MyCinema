package ru.project.myCinema.dto.movie;

import ru.project.myCinema.model.MovieAgeRating;

public record MovieRequest(
        String title,
        MovieAgeRating ageRating,
        Integer minutesCount
) {
}
