package ru.project.myCinema.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Место
 */
@Entity
@Table(name = "seat")
public class Seat extends BaseEntity{

    /**
     * Номер
     */
    private Integer number;

    /**
     * Зал
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    /**
     * Заказы
     */
    @ManyToMany(mappedBy = "seats")
    private Set<Booking> bookings = new HashSet<>();

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }
}
