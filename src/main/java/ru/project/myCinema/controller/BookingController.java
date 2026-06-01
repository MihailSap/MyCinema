package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.booking.BookingCreateRequest;
import ru.project.myCinema.dto.seat.SeatIdResponse;
import ru.project.myCinema.mapper.SeatMapper;
import ru.project.myCinema.model.Booking;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.model.Seat;
import ru.project.myCinema.model.Session;
import ru.project.myCinema.service.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для работы с заказами
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final AuthService authService;
    private final BookingService bookingService;
    private final SessionService sessionService;
    private final PersonService personService;
    private final SeatService seatService;
    private final SeatMapper seatMapper;

    @Autowired
    public BookingController(
            AuthService authService,
            BookingService bookingService,
            SessionService sessionService,
            PersonService personService,
            SeatService seatService,
            SeatMapper seatMapper
    ) {
        this.authService = authService;
        this.bookingService = bookingService;
        this.sessionService = sessionService;
        this.personService = personService;
        this.seatService = seatService;
        this.seatMapper = seatMapper;
    }

    /**
     * Страница создания заказа
     */
    @GetMapping("/new")
    public String createPage(@RequestParam Long sessionId, Model model) {
        Session session = sessionService.getById(sessionId);
        List<Seat> availableSeats = seatService.getAvailableSeatsBySession(session);
        List<SeatIdResponse> seatIdResponses = seatMapper.mapToSeatIdResponses(availableSeats);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("availableSeats", seatIdResponses);

        return "bookings/create";
    }

    /**
     * Создание заказа
     */
    @PostMapping("/new")
    public String create(@ModelAttribute BookingCreateRequest request, Model model) {
        Person person = authService.getAuthenticatedPerson();
        Session session = sessionService.getById(request.sessionId());
        Seat seat = seatService.getById(request.seatId());

        if (session.getStartDateTime().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Нельзя забронировать место на прошедший сеанс");
            return "redirect:/bookings/new";
        }
        if (!seat.getHall().getId().equals(session.getHall().getId())) {
            model.addAttribute("error", "Место не принадлежит залу данного сеанса");
            return "redirect:/bookings/new";
        }
        if (seatService.isSeatBooked(seat, session)) {
            model.addAttribute("error", "Место занято");
            return "redirect:/bookings/new";
        }

        bookingService.create(person, session, seat);
        return "redirect:/persons/me";
    }

    /**
     * Оплата заказа
     */
    @PostMapping("/{id}/pay")
    public String pay(@PathVariable("id") Long id, Model model) {
        Person person = authService.getAuthenticatedPerson();
        Booking booking = bookingService.getById(id);
        double resultPrice = booking.getSeats().size() * booking.getSession().getTicketPrice();

        personService.reduceBalance(person, resultPrice);
        bookingService.pay(booking);

        return "redirect:/persons/me";
    }

    /**
     * Отмена заказа
     */
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable("id") Long id) {
        Booking booking = bookingService.getById(id);
        bookingService.cancel(booking);
        return "redirect:/persons/me";
    }
}
