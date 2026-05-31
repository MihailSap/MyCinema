package ru.project.myCinema.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.HallRequest;
import ru.project.myCinema.dto.HallResponse;
import ru.project.myCinema.dto.SessionResponseDto;
import ru.project.myCinema.mapper.HallMapper;
import ru.project.myCinema.mapper.SessionMapper;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.service.HallService;
import ru.project.myCinema.service.SessionService;

import java.util.List;

/**
 * Контроллер для работы с залами
 */
@Controller
@RequestMapping("/halls")
public class HallController {

    private final HallService hallService;
    private final HallMapper hallMapper;
    private final SessionService sessionService;
    private final SessionMapper sessionMapper;

    public HallController(
            HallService hallService,
            HallMapper hallMapper,
            SessionService sessionService,
            SessionMapper sessionMapper
    ) {
        this.hallService = hallService;
        this.hallMapper = hallMapper;
        this.sessionService = sessionService;
        this.sessionMapper = sessionMapper;
    }

    /**
     * Страница с залами
     */
    @GetMapping
    public String halls(Model model) {
        List<Hall> halls = hallService.getAllHalls();
        List<HallResponse> hallResponses = hallMapper.mapToHallResponses(halls);
        model.addAttribute("halls", hallResponses);
        return "halls/list";
    }

    /**
     * Страница конкретного зала
     */
    @GetMapping("/{id}")
    public String hall(@PathVariable("id") Long id, Model model) {
        Hall hall = hallService.getById(id);
        HallResponse hallResponse = hallMapper.mapToHallResponse(hall);
        model.addAttribute("hall", hallResponse);

        List<Session> actualSessions = sessionService.getActualByHall(hall);
        List<SessionResponseDto> sessionResponseDtos = sessionMapper.mapToSessionResponseDtos(actualSessions);
        model.addAttribute("sessions", sessionResponseDtos);

        return "halls/details";
    }

    /**
     * Страница создания зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("hall", new HallRequest(null, null, List.of()));
        return "halls/create";
    }

    /**
     * Создание зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String create(@ModelAttribute HallRequest hallRequest) {
        Hall hall = hallService.create(hallRequest);
        return "redirect:/halls/" + hall.getId();
    }

    /**
     * Страница редактирования зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        Hall hall = hallService.getById(id);
        HallRequest request = new HallRequest(
                hall.getNumber(),
                hall.getCapacity(),
                hall.getSeats()
                        .stream()
                        .map(Seat::getNumber)
                        .toList()
        );

        model.addAttribute("hallId", id);
        model.addAttribute("hall", request);
        return "halls/edit";
    }

    /**
     * Редактирование зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute HallRequest hallRequest) {
        Hall hall = hallService.getById(id);
        hallService.update(hallRequest, hall);
        return "redirect:/halls/" + id;
    }

    /**
     * Удаление зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Hall hall = hallService.getById(id);
        hallService.delete(hall);
        return "redirect:/admin";
    }
}
