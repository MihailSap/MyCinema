package ru.project.myCinema.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Зал
 */
@Entity
@Table(name = "hall")
public class Hall extends BaseEntity{

    /**
     * Номер
     */
    private Integer number;

    /**
     * Вместимость
     */
    private Integer capacity;

    /**
     * Сеансы, проходящие в зале
     */
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions = new HashSet<>();

    /**
     * Места, имеющиеся в зале
     */
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats = new HashSet<>();

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }
}
