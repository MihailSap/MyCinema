package ru.project.myCinema.service.Impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.project.myCinema.dto.session.SessionRequest;
import ru.project.myCinema.exception.NotFoundException;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.MovieAgeRating;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.repository.HallRepository;
import ru.project.myCinema.repository.MovieRepository;
import ru.project.myCinema.repository.SessionRepository;
import ru.project.myCinema.service.SessionService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Тесты сервиса для работы с сеансом
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SessionServiceImplTest {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private MovieRepository movieRepository;

    /**
     * Проверка получения существующего сеанса по id
     */
    @Test
    void testGetByIdSuccess() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("movie");
        Session session = createSession(hall, movie, LocalDateTime.now().plusDays(1));

        Session foundSession = sessionService.getById(session.getId());

        Assertions.assertNotNull(foundSession);
        Assertions.assertEquals(session, foundSession);
    }

    /**
     * Проверка получения несуществующего сеанса по id
     */
    @Test
    void testGetByIdFailure() {
        Assertions.assertThrows(NotFoundException.class, () -> sessionService.getById(999L));
    }

    /**
     * Проверка получения всех сеансов
     */
    @Test
    void testGetAllSessions() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("movie");

        createSession(hall, movie, LocalDateTime.now().plusDays(1));
        createSession(hall, movie, LocalDateTime.now().plusDays(2));

        List<Session> sessions = sessionService.getAllSessions();

        Assertions.assertNotNull(sessions);
        Assertions.assertEquals(2, sessions.size());
    }

    /**
     * Проверка получения актуальных сеансов
     */
    @Test
    void testGetActualSessions() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("movie");

        createSession(hall, movie, LocalDateTime.now().minusDays(1));
        createSession(hall, movie, LocalDateTime.now().plusDays(1));

        List<Session> sessions = sessionService.getActualSessions();

        Assertions.assertNotNull(sessions);
        Assertions.assertEquals(1, sessions.size());
    }

    /**
     * Проверка получения актуальных сеансов по фильму
     */
    @Test
    void testGetActualByMovie() {
        Hall hall = createHall(1, 50);
        Movie movie1 = createMovie("movie1");
        Movie movie2 = createMovie("movie2");

        createSession(hall, movie1, LocalDateTime.now().plusDays(1));
        createSession(hall, movie2, LocalDateTime.now().plusDays(1));

        List<Session> sessions = sessionService.getActualByMovie(movie1);
        Assertions.assertNotNull(sessions);
        Assertions.assertEquals(1, sessions.size());
        Assertions.assertEquals(movie1, sessions.getFirst().getMovie());
    }

    /**
     * Проверка получения актуальных сеансов по залу
     */
    @Test
    void testGetActualByHall() {
        Hall hall1 = createHall(1, 50);
        Hall hall2 = createHall(2, 100);

        Movie movie = createMovie("movie");
        createSession(hall1, movie, LocalDateTime.now().plusDays(1));
        createSession(hall2, movie, LocalDateTime.now().plusDays(1));

        List<Session> sessions = sessionService.getActualByHall(hall1);
        Assertions.assertNotNull(sessions);
        Assertions.assertEquals(1, sessions.size());
        Assertions.assertEquals(hall1, sessions.getFirst().getHall());
    }

    /**
     * Проверка создания сеанса
     */
    @Test
    void testCreateSession() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("movie");
        SessionRequest request = new SessionRequest(500.0, "2030-01-01T12:00:00", hall.getId(), movie.getId());
        Session session = sessionService.create(request, hall, movie);

        Assertions.assertNotNull(session);
        Assertions.assertNotNull(session.getId());
        Assertions.assertEquals(500.0, session.getTicketPrice());
        Assertions.assertEquals(hall, session.getHall());
        Assertions.assertEquals(movie, session.getMovie());
    }

    /**
     * Проверка полного обновления сеанса
     */
    @Test
    void testUpdateSession() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("movie");
        Session session = createSession(hall, movie, LocalDateTime.of(2030, 1, 1, 12, 0));
        SessionRequest request = new SessionRequest(700.0, "2030-02-01T15:00:00", null, null);
        Session updatedSession = sessionService.update(request, session);

        Assertions.assertNotNull(updatedSession);
        Assertions.assertEquals(700.0, updatedSession.getTicketPrice());
        Assertions.assertEquals(LocalDateTime.of(2030, 2, 1, 15, 0), updatedSession.getStartDateTime());
    }

    /**
     * Проверка обновления только цены
     */
    @Test
    void testUpdateOnlyPrice() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("Movie");
        Session session = createSession(hall, movie, LocalDateTime.of(2030, 1, 1, 12, 0));
        SessionRequest request = new SessionRequest(900.0, null, null, null);
        Session updatedSession = sessionService.update(request, session);

        Assertions.assertNotNull(updatedSession);
        Assertions.assertEquals(900.0, updatedSession.getTicketPrice());
        Assertions.assertEquals(LocalDateTime.of(2030, 1, 1, 12, 0), updatedSession.getStartDateTime());
    }

    /**
     * Проверка обновления только даты
     */
    @Test
    void testUpdateOnlyDateTime() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("Movie");
        Session session = createSession(hall, movie, LocalDateTime.of(2030, 1, 1, 12, 0));
        SessionRequest request = new SessionRequest(null, "2030-03-01T18:00:00", null, null);
        Session updatedSession = sessionService.update(request, session);

        Assertions.assertNotNull(updatedSession);
        Assertions.assertEquals(100.0, updatedSession.getTicketPrice());
        Assertions.assertEquals(LocalDateTime.of(2030, 3, 1, 18, 0), updatedSession.getStartDateTime());
    }

    /**
     * Проверка смены зала
     */
    @Test
    void testUpdateHall() {
        Hall hall1 = createHall(1, 50);
        Hall hall2 = createHall(2, 100);

        Movie movie = createMovie("Movie");
        Session session = createSession(hall1, movie, LocalDateTime.now().plusDays(1));
        Session updatedSession = sessionService.updateHall(session, hall2);

        Assertions.assertNotNull(updatedSession);
        Assertions.assertEquals(hall2.getId(), updatedSession.getHall().getId());
    }

    /**
     * Проверка смены фильма
     */
    @Test
    void testUpdateMovie() {
        Hall hall = createHall(1, 50);
        Movie movie1 = createMovie("Movie1");
        Movie movie2 = createMovie("Movie2");

        Session session = createSession(hall, movie1, LocalDateTime.now().plusDays(1));
        Session updatedSession = sessionService.updateMovie(session, movie2);

        Assertions.assertNotNull(updatedSession);
        Assertions.assertEquals(movie2.getId(), updatedSession.getMovie().getId());
    }

    /**
     * Проверка удаления сеанса
     */
    @Test
    void testDeleteSession() {
        Hall hall = createHall(1, 50);
        Movie movie = createMovie("Movie");
        Session session = createSession(hall, movie, LocalDateTime.now().plusDays(1));
        sessionService.delete(session);

        Assertions.assertFalse(sessionRepository.findById(session.getId()).isPresent());
    }

    /**
     * Вспомогательный метод для создания зала
     */
    private Hall createHall(Integer number, Integer capacity) {
        Hall hall = new Hall();
        hall.setNumber(number);
        hall.setCapacity(capacity);
        return hallRepository.save(hall);
    }

    /**
     * Вспомогательный метод для создания фильма
     */
    private Movie createMovie(String title) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setAgeRating(MovieAgeRating.PG13);
        movie.setMinutesLength(120);
        return movieRepository.save(movie);
    }

    /**
     * Вспомогательный метод для создания сеанса
     */
    private Session createSession(Hall hall, Movie movie, LocalDateTime startDateTime) {
        Session session = new Session();
        session.setHall(hall);
        session.setMovie(movie);
        session.setTicketPrice(100.0);
        session.setStartDateTime(startDateTime);

        return sessionRepository.save(session);
    }
}