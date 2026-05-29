package ru.project.myCinema.dto;

import ru.project.myCinema.model.MovieAgeRating;

public record MovieRequest(
        String title,
        MovieAgeRating ageRating,
        Integer minutesCount
) {
}
