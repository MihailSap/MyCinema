package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.myCinema.dto.HallResponse;
import ru.project.myCinema.dto.MovieResponse;
import ru.project.myCinema.dto.PersonResponse;
import ru.project.myCinema.dto.SessionResponseDto;
import ru.project.myCinema.mapper.HallMapper;
import ru.project.myCinema.mapper.MovieMapper;
import ru.project.myCinema.mapper.PersonMapper;
import ru.project.myCinema.mapper.SessionMapper;
import ru.project.myCinema.model.*;
import ru.project.myCinema.service.HallService;
import ru.project.myCinema.service.MovieService;
import ru.project.myCinema.service.PersonService;
import ru.project.myCinema.service.SessionService;

import java.util.List;

/**
 * Контроллер для работы с панелью администратора
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PersonService personService;
    private final PersonMapper personMapper;
    private final HallService hallService;
    private final HallMapper hallMapper;
    private final SessionService sessionService;
    private final SessionMapper sessionMapper;
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Autowired
    public AdminController(
            PersonService personService,
            PersonMapper personMapper,
            HallService hallService,
            HallMapper hallMapper,
            SessionService sessionService,
            SessionMapper sessionMapper,
            MovieService movieService,
            MovieMapper movieMapper
    ) {
        this.personService = personService;
        this.personMapper = personMapper;
        this.hallService = hallService;
        this.hallMapper = hallMapper;
        this.sessionService = sessionService;
        this.sessionMapper = sessionMapper;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    /**
     * Страница панели администратора
     */
    @GetMapping
    public String adminPanel(Model model) {
        List<Person> persons = personService.getAll();
        List<PersonResponse> personResponses = personMapper.mapToPersonResponses(persons);
        model.addAttribute("persons", personResponses);

        List<Hall> halls = hallService.getAllHalls();
        List<HallResponse> hallResponses = hallMapper.mapToHallResponses(halls);
        model.addAttribute("halls", hallResponses);

        List<Movie> movies = movieService.getAllMovies();
        List<MovieResponse> movieResponses = movieMapper.mapToMovieResponses(movies);
        model.addAttribute("movies", movieResponses);

        List<Session> sessions = sessionService.getActualSessions();
        List<SessionResponseDto> sessionResponseDtos = sessionMapper.mapToSessionResponseDtos(sessions);
        model.addAttribute("sessions", sessionResponseDtos);

        return "admin/admin-panel";
    }

    /**
     * Блокировка пользователя
     */
    @PostMapping("/{personId}/block")
    public String block(@PathVariable("personId") Long personId) {
        Person person = personService.getById(personId);
        if(PersonAccountStatus.BLOCKED.equals(person.getAccountStatus())) {
            throw new RuntimeException("Пользователь уже заблокирован");
        }
        personService.block(person);
        return "redirect:/admin";
    }

    /**
     * Разблокировка пользователя
     */
    @PostMapping("/{personId}/unblock")
    public String unblock(@PathVariable("personId") Long personId) {
        Person person = personService.getById(personId);
        if(PersonAccountStatus.ACTIVE.equals(person.getAccountStatus())) {
            throw new RuntimeException("Пользователь не заблокирован");
        }
        personService.unblock(person);
        return "redirect:/admin";
    }

    /**
     * Получение данных пользователя по его id
     */
    @GetMapping("/{personId}")
    public String getPersonById(@PathVariable("personId") Long personId, Model model) {
        Person person = personService.getById(personId);
        PersonResponse response = personMapper.mapToPersonResponse(person);
        model.addAttribute("person", response);
        return "person/details-another";
    }
}
