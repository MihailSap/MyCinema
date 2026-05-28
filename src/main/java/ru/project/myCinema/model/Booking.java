package ru.project.myCinema.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Заказ
 */
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {

    /**
     * Время создания
     */
    private LocalDateTime createdAt;

    /**
     * Статус
     */
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    /**
     * Создатель заказа
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    /**
     * Сеанс
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    /**
     * Забронированные места
     */
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "booking_seats",
            joinColumns = { @JoinColumn(name = "booking_id") },
            inverseJoinColumns = { @JoinColumn(name = "seat_id") }
    )
    private Set<Seat> seats = new HashSet<>();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }
}
