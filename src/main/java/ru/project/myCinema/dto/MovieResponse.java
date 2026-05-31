package ru.project.myCinema.dto;

import ru.project.myCinema.model.MovieAgeRating;

public record MovieResponse(
        Long id,
        String title,
        MovieAgeRating ageRating,
        Integer minutesLength
) {
}
