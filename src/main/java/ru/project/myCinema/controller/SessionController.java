package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.hall.HallResponse;
import ru.project.myCinema.dto.movie.MovieResponse;
import ru.project.myCinema.dto.seat.SeatStatusResponse;
import ru.project.myCinema.dto.session.SessionRequest;
import ru.project.myCinema.dto.session.SessionResponseDto;
import ru.project.myCinema.mapper.HallMapper;
import ru.project.myCinema.mapper.MovieMapper;
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
 * Контроллер для работы с сеансами
 */
@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final HallService hallService;
    private final HallMapper hallMapper;
    private final SessionService sessionService;
    private final SessionMapper sessionMapper;
    private final SeatMapper seatMapper;
    private final SeatService seatService;

    @Autowired
    public SessionController(
            MovieService movieService,
            MovieMapper movieMapper,
            HallService hallService,
            HallMapper hallMapper,
            SessionService sessionService,
            SessionMapper sessionMapper,
            SeatMapper seatMapper,
            SeatService seatService
    ) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.hallService = hallService;
        this.hallMapper = hallMapper;
        this.sessionService = sessionService;
        this.sessionMapper = sessionMapper;
        this.seatMapper = seatMapper;
        this.seatService = seatService;
    }

    /**
     * Страница с предстоящими сеансами
     */
    @GetMapping("/actual")
    public String actualSessions(Model model) {
        List<Session> actualSessions = sessionService.getActualSessions();
        List<SessionResponseDto> actualSessionsResponsesDto =
                sessionMapper.mapToSessionResponseDtos(actualSessions);
        model.addAttribute("sessions", actualSessionsResponsesDto);
        return "sessions/list";
    }

    /**
     * Страница создания нового сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createPage(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        List<MovieResponse> movieResponses = movieMapper.mapToMovieResponses(movies);
        model.addAttribute("movies", movieResponses);

        List<Hall> halls = hallService.getAllHalls();
        List<HallResponse> hallResponses = hallMapper.mapToHallResponses(halls);
        model.addAttribute("halls", hallResponses);

        model.addAttribute("session", new SessionRequest(null, null, null, null));
        return "sessions/create";
    }

    /**
     * Создание нового сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String create(
            @ModelAttribute SessionRequest sessionRequest
    ) {
        Hall hall = hallService.getById(sessionRequest.hallId());
        Movie movie = movieService.getById(sessionRequest.movieId());
        sessionService.create(sessionRequest, hall, movie);
        return "redirect:/sessions/actual";
    }

    /**
     * Страница редактирования сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editPage(
            @PathVariable("id") Long id,
            Model model
    ) {
        Session session = sessionService.getById(id);
        SessionRequest request = new SessionRequest(
                session.getTicketPrice(),
                session.getStartDateTime().toString(),
                session.getHall().getId(),
                session.getMovie().getId()
        );

        model.addAttribute("sessionId", id);
        model.addAttribute("session", request);

        return "sessions/edit";
    }

    /**
     * Редактирование сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute SessionRequest sessionRequest) {
        Session session = sessionService.getById(id);
        sessionService.update(sessionRequest, session);
        return "redirect:/sessions/actual";
    }

    /**
     * Удаление сеанса
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Session session = sessionService.getById(id);
        sessionService.delete(session);
        return "redirect:/admin";
    }

    /**
     * Страница конкретного сеанса
     */
    @GetMapping("/{id}")
    public String sessionPage(@PathVariable("id") Long id, Model model) {
        Session session = sessionService.getById(id);
        List<SeatStatusResponse> seatStatusResponses = new ArrayList<>();
        for (Seat seat : session.getHall().getSeats()) {
            boolean isSeatBooked = seatService.isSeatBooked(seat, session);
            SeatStatusResponse seatStatusResponse = seatMapper.mapToSeatStatusResponse(seat, isSeatBooked);
            seatStatusResponses.add(seatStatusResponse);
        }

        model.addAttribute("sessionDto", sessionMapper.mapToSessionResponseDto(session));
        model.addAttribute("seats", seatStatusResponses);
        return "sessions/details";
    }
}
