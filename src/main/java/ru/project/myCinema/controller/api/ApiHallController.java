package ru.project.myCinema.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.DefaultResponse;
import ru.project.myCinema.dto.hall.HallRequest;
import ru.project.myCinema.dto.hall.HallResponse;
import ru.project.myCinema.mapper.HallMapper;
import ru.project.myCinema.mapper.SeatMapper;
import ru.project.myCinema.model.Hall;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.service.HallService;
import ru.project.myCinema.service.SeatService;

import java.util.List;

/**
 * Контроллер для управления залами
 */
@RestController
@RequestMapping("/api/halls")
public class ApiHallController {

    private final HallService hallService;
    private final HallMapper hallMapper;
    private final SeatService seatService;
    private final SeatMapper seatMapper;

    @Autowired
    public ApiHallController(
            HallService hallService,
            HallMapper hallMapper,
            SeatService seatService,
            SeatMapper seatMapper) {
        this.hallService = hallService;
        this.hallMapper = hallMapper;
        this.seatService = seatService;
        this.seatMapper = seatMapper;
    }

    /**
     * Получение зала по id
     */
    @GetMapping("/{hallId}")
    public HallResponse getById(@PathVariable("hallId") Long hallId){
        Hall hall = hallService.getById(hallId);
        return hallMapper.mapToHallResponse(hall);
    }

    /**
     * Получение всех залов
     */
    @GetMapping
    public List<HallResponse> getHalls(){
        List<Hall> halls = hallService.getAllHalls();
        return hallMapper.mapToHallResponses(halls);
    }

    /**
     * Получение номеров мест в зале
     */
    @GetMapping("/{hallId}/seats")
    public List<Integer> getSeatsNumbers(@PathVariable("hallId") Long hallId){
        Hall hall = hallService.getById(hallId);
        List<Seat> seats = seatService.getSeatsByHall(hall);
        return seatMapper.mapToSeatsNumbers(seats);
    }

    /**
     * Создание зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public HallResponse create(@RequestBody HallRequest hallRequest){
        Hall hall = hallService.create(hallRequest);
        return hallMapper.mapToHallResponse(hall);
    }

    /**
     * Удаление зала
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{hallId}")
    public DefaultResponse delete(@PathVariable("hallId") Long hallId){
        Hall hall = hallService.getById(hallId);
        hallService.delete(hall);
        return new DefaultResponse("Зал с id=%s удален".formatted(hallId));
    }
}
