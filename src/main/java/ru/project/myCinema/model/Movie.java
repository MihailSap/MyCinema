package ru.project.myCinema.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Фильм
 */
@Entity
@Table(name = "movie")
public class Movie extends BaseEntity{

    /**
     * Название
     */
    private String title;

    /**
     * Возрастное ограничение
     */
    @Enumerated(EnumType.STRING)
    private MovieAgeRating ageRating;

    /**
     * Длительность в минутах
     */
    private Integer minutesCount;

    /**
     * Сеансы
     */
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions = new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieAgeRating getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(MovieAgeRating ageRating) {
        this.ageRating = ageRating;
    }

    public Integer getMinutesCount() {
        return minutesCount;
    }

    public void setMinutesCount(Integer minutesCount) {
        this.minutesCount = minutesCount;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}
