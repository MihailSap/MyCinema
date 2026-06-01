package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.exception.NotFoundException;
import ru.project.myCinema.model.*;
import ru.project.myCinema.repository.BookingRepository;
import ru.project.myCinema.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Реализация интерфейса для работы с заказом
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с id=%s не найден".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getPendingActualByPerson(Person person) {
        return bookingRepository.findActiveBookings(person, BookingStatus.PENDING, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> getDoneActualByPerson(Person person) {
        return bookingRepository.findActiveBookings(person, BookingStatus.DONE, LocalDateTime.now());
    }

    @Transactional
    @Override
    public Booking pay(Booking booking) {
        booking.setStatus(BookingStatus.DONE);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking cancel(Booking booking) {
        booking.setStatus(BookingStatus.CANCELED);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking create(Person person, Session session, Seat seat) {
        Booking booking = new Booking();
        booking.setPerson(person);
        booking.setSession(session);
        booking.setSeats(Set.of(seat));
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }
}
