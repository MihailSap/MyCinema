package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.BookingCreateRequest;
import ru.project.myCinema.dto.SeatShortResponse;
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
        List<SeatShortResponse> seatShortResponses = seatMapper.mapToSeatShortResponses(availableSeats);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("availableSeats", seatShortResponses);

        return "bookings/create";
    }

    /**
     * Создание заказа
     */
    @PostMapping("/new")
    public String create(@ModelAttribute BookingCreateRequest request) {
        Person person = authService.getAuthenticatedPerson();
        Session session = sessionService.getById(request.sessionId());
        Seat seat = seatService.getById(request.seatId());

        if (session.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Нельзя забронировать место на прошедший сеанс");
        }
        if (!seat.getHall().getId().equals(session.getHall().getId())) {
            throw new RuntimeException("Место не принадлежит залу данного сеанса");
        }
        if (seatService.isSeatBooked(seat, session)) {
            throw new RuntimeException("Место занято");
        }

        bookingService.create(person, session, seat);
        return "redirect:/persons/me";
    }

    /**
     * Оплата заказа
     */
    @PostMapping("/{id}/pay")
    public String pay(@PathVariable("id") Long id) {
        Person person = authService.getAuthenticatedPerson();
        Booking booking = bookingService.getById(id);
        double resultPrice = booking.getSeats().size() * booking.getSession().getTicketPrice();
        if (person.getBalance() < resultPrice) {
            throw new RuntimeException("Недостаточно средств");
        }

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
