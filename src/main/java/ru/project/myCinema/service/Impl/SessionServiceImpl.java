package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.SessionRequest;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.repository.SessionRepository;
import ru.project.myCinema.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация интерфейса для работы с сеансом
 */
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Session getById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сеанс с id=%s не найден".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> getActualSessions() {
        return sessionRepository.findByStartDateTimeAfter(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> getActualByMovie(Movie movie) {
        return sessionRepository.findByMovieAndStartDateTimeAfter(movie, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> getActualByHall(Hall hall) {
        return sessionRepository.findByHallAndStartDateTimeAfter(hall, LocalDateTime.now());
    }

    @Transactional
    @Override
    public Session create(SessionRequest sessionRequest, Hall hall, Movie movie) {
        Session session = new Session();
        session.setTicketPrice(sessionRequest.ticketPrice());
        session.setStartDateTime(LocalDateTime.parse(sessionRequest.startDateTime()));
        session.setHall(hall);
        session.setMovie(movie);
        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Session update(SessionRequest sessionRequest, Session session) {
        Double ticketPrice = sessionRequest.ticketPrice();
        if(ticketPrice != null) {
            session.setTicketPrice(ticketPrice);
        }

        String startDateTime = sessionRequest.startDateTime();
        if(startDateTime != null && !startDateTime.isEmpty()) {
            session.setStartDateTime(LocalDateTime.parse(startDateTime));
        }

        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Session updateHall(Session session, Hall hall) {
        session.setHall(hall);
        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Session updateMovie(Session session, Movie movie) {
        session.setMovie(movie);
        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }
}
