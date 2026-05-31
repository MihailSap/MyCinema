package ru.project.myCinema.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.*;
import ru.project.myCinema.dto.seat.SeatResponse;
import ru.project.myCinema.dto.session.SessionRequest;
import ru.project.myCinema.dto.session.SessionResponse;
import ru.project.myCinema.dto.session.SessionResponseDto;
import ru.project.myCinema.mapper.SeatMapper;
import ru.project.myCinema.mapper.SessionMapper;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Movie;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.service.HallService;
import ru.project.myCinema.service.MovieService;
import ru.project.myCinema.service.SeatService;
import ru.project.myCinema.service.SessionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для управления сеансами
 */
@RestController
@RequestMapping("/api/sessions")
public class ApiSessionController {

    private final MovieService movieService;
    private final HallService hallService;
    private final SessionService sessionService;
    private final SessionMapper sessionMapper;
    private final SeatMapper seatMapper;
    private final SeatService seatService;

    @Autowired
    public ApiSessionController(
            MovieService movieService,
            HallService hallService,
            SessionService sessionService,
            SessionMapper sessionMapper,
            SeatMapper seatMapper,
            SeatService seatService
    ) {
        this.movieService = movieService;
        this.hallService = hallService;
        this.sessionService = sessionService;
        this.sessionMapper = sessionMapper;
        this.seatMapper = seatMapper;
        this.seatService = seatService;
    }

    /**
     * Получение сеанса по id
     */
    @GetMapping("/{sessionId}")
    public SessionResponse getById(@PathVariable("sessionId") Long sessionId){
        Session session = sessionService.getById(sessionId);
        return sessionMapper.mapToSessionResponse(session);
    }

    /**
     * Получение мест с их статусом по сеансу
     */
    @GetMapping("/{sessionId}/seats")
    public List<SeatResponse> getSeatsBySessionId(@PathVariable("sessionId") Long sessionId){
        Session session = sessionService.getById(sessionId);
        List<SeatResponse> seatResponses = new ArrayList<>();
        for(Seat seat : session.getHall().getSeats()){
            boolean isSeatBooked = seatService.isSeatBooked(seat, session);
            seatResponses.add(seatMapper.mapToSeatResponse(seat, isSeatBooked));
        }
        return seatResponses;
    }

    /**
     * Получение предстоящих сеансов
     */
    @GetMapping("/actual")
    public List<SessionResponseDto> getAllActual(){
        List<Session> sessions = sessionService.getActualSessions();
        return sessionMapper.mapToSessionResponseDtos(sessions);
    }

    /**
     * Получение всех предстоящих сеансов фильма
     */
    @GetMapping("/actual/by-movie/{movieId}")
    public List<SessionResponse> getAllActualByMovie(@PathVariable("movieId") Long movieId){
        Movie movie = movieService.getById(movieId);
        List<Session> sessions = sessionService.getActualByMovie(movie);
        return sessionMapper.mapToSessionResponses(sessions);
    }

    /**
     * Получение всех предстоящих сеансов в зале
     */
    @GetMapping("/actual/by-hall/{hallId}")
    public List<SessionResponse> getAllActualByHall(@PathVariable("hallId") Long hallId){
        Hall hall = hallService.getById(hallId);
        List<Session> sessions = sessionService.getActualByHall(hall);
        return sessionMapper.mapToSessionResponses(sessions);
    }

    /**
     * Создание сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SessionResponse create(@RequestBody SessionRequest sessionRequest){
        Hall hall = hallService.getById(sessionRequest.hallId());
        Movie movie = movieService.getById(sessionRequest.movieId());
        Session session = sessionService.create(sessionRequest, hall, movie);
        return sessionMapper.mapToSessionResponse(session);
    }

    /**
     * Редактирование данных сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{sessionId}")
    public SessionResponse update(
            @PathVariable("sessionId") Long sessionId,
            @RequestBody SessionRequest sessionRequest
    ){
        Session session = sessionService.getById(sessionId);
        Session updatedSession = sessionService.update(sessionRequest, session);
        return sessionMapper.mapToSessionResponse(updatedSession);
    }

    /**
     * Смена зала, в котором будет проходить сеанс
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{sessionId}/hall")
    public SessionResponse updateHall(
            @PathVariable("sessionId") Long sessionId,
            @RequestBody SessionRequest sessionRequest
    ){
        Hall hall = hallService.getById(sessionRequest.hallId());
        Session session = sessionService.getById(sessionId);
        Session updatedSession = sessionService.updateHall(session, hall);
        return sessionMapper.mapToSessionResponse(updatedSession);
    }

    /**
     * Смена фильма, который будет показан на сеансе
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{sessionId}/movie")
    public SessionResponse updateMovie(
            @PathVariable("sessionId") Long sessionId,
            @RequestBody SessionRequest sessionRequest
    ){
        Movie movie = movieService.getById(sessionRequest.movieId());
        Session session = sessionService.getById(sessionId);
        Session updatedSession = sessionService.updateMovie(session, movie);
        return sessionMapper.mapToSessionResponse(updatedSession);
    }

    /**
     * Удаление сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{sessionId}")
    public DefaultResponse delete(@PathVariable("sessionId") Long sessionId){
        Session session = sessionService.getById(sessionId);
        sessionService.delete(session);
        return new DefaultResponse("Сеанс с id=%s успешно удален".formatted(session));
    }
}
