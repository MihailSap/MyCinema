package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.model.BookingStatus;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.repository.SeatRepository;
import ru.project.myCinema.service.SeatService;

import java.util.List;

/**
 * Реализация интерфейса для работы с местом
 */
@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Seat getById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Место с id=%s не найдено".formatted(id)));
    }

    @Override
    public boolean isSeatBooked(Seat seat, Session session) {
        return seat.getBookings().stream()
                .filter(booking -> booking.getStatus() != BookingStatus.CANCELED)
                .anyMatch(booking -> booking.getSession().getId().equals(session.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Seat> getSeatsByHall(Hall hall) {
        return seatRepository.findByHall(hall);
    }

    @Override
    public List<Seat> getAvailableSeatsBySession(Session session) {
        return session.getHall()
                .getSeats()
                .stream()
                .filter(seat -> !isSeatBooked(seat, session))
                .toList();
    }

    @Transactional
    @Override
    public void createByHall(List<Integer> seatsNumbers, Hall hall) {
        for (Integer seatNumber : seatsNumbers) {
            Seat seat = new Seat();
            seat.setHall(hall);
            seat.setNumber(seatNumber);
            seatRepository.save(seat);
        }
    }

    @Transactional
    @Override
    public void createByHall(Integer seatNumber, Hall hall) {
        Seat seat = new Seat();
        seat.setHall(hall);
        seat.setNumber(seatNumber);
        seatRepository.save(seat);
    }

    @Transactional
    @Override
    public void deleteByHall(Hall hall) {
        seatRepository.deleteByHall(hall);
    }

    @Transactional
    @Override
    public void delete(Seat seat) {
        seatRepository.delete(seat);
    }

    @Transactional
    @Override
    public void deleteByHallAndNumberGreaterThan(Hall hall, Integer seatNumber) {
        seatRepository.deleteByHallAndNumberGreaterThan(hall, seatNumber);
    }
}
