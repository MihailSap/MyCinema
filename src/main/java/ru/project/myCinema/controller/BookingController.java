package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.BookingCreateRequest;
import ru.project.myCinema.dto.BookingResponse;
import ru.project.myCinema.mapper.BookingMapper;
import ru.project.myCinema.model.*;
import ru.project.myCinema.service.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для управления заказами
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final AuthService authService;
    private final BookingService bookingService;
    private final SessionService sessionService;
    private final PersonService personService;
    private final SeatService seatService;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(
            AuthService authService,
            BookingService bookingService,
            SessionService sessionService,
            PersonService personService,
            SeatService seatService,
            BookingMapper bookingMapper
    ) {
        this.authService = authService;
        this.bookingService = bookingService;
        this.sessionService = sessionService;
        this.personService = personService;
        this.seatService = seatService;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Получение заказа по id
     */
    @GetMapping("/{bookingId}")
    public BookingResponse getById(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingService.getById(bookingId);
        return bookingMapper.mapToBookingResponse(booking);
    }

    /**
     * Создание заказа
     */
    @PostMapping
    public BookingResponse create(@RequestBody BookingCreateRequest bookingCreateRequest){
        Person person = authService.getAuthenticatedPerson();
        Session session = sessionService.getById(bookingCreateRequest.sessionId());
        Seat seat = seatService.getById(bookingCreateRequest.seatId());
        if(session.getStartDateTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Нельзя забронировать место на прошедший сеанс");
        }
        if(!seat.getHall().getId().equals(session.getHall().getId())){
            throw new RuntimeException("Место не принадлежит залу данного сеанса");
        }
        if(seatService.isSeatBooked(seat, session)){
            throw new RuntimeException("Место занято");
        }

        Booking booking = bookingService.create(person, session, seat);
        return bookingMapper.mapToBookingResponse(booking);
    }

    /**
     * Получение всех неоплаченных заказов авторизованного пользователя
     */
    @GetMapping("/actual/pending")
    public List<BookingResponse> getMyPendingActual(){
        Person person = authService.getAuthenticatedPerson();
        List<Booking> bookings = bookingService.getPendingActualByPerson(person);
        return bookingMapper.mapToBookingResponses(bookings);
    }

    /**
     * Получение всех оплаченных заказов авторизованного пользователя
     */
    @GetMapping("/actual/done")
    public List<BookingResponse> getMyDoneActual(){
        Person person = authService.getAuthenticatedPerson();
        List<Booking> bookings = bookingService.getDoneActualByPerson(person);
        return bookingMapper.mapToBookingResponses(bookings);
    }

    /**
     * Оплата заказа
     */
    @PatchMapping("/{bookingId}/pay")
    public BookingResponse pay(@PathVariable("bookingId") Long bookingId){
        Person person = authService.getAuthenticatedPerson();
        Booking booking = bookingService.getById(bookingId);
        double resultPrice = booking.getSeats().size() * booking.getSession().getTicketPrice();
        if(Double.compare(person.getBalance(), resultPrice) < 0){
            throw new RuntimeException("Недостаточно денег на балансе для оплаты заказа");
        }

        personService.reduceBalance(person, resultPrice);
        Booking updatedBooking = bookingService.pay(booking);
        return bookingMapper.mapToBookingResponse(updatedBooking);
    }

    /**
     * Отмена заказа
     */
    @PatchMapping("/{bookingId}/cancel")
    public BookingResponse cancel(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingService.getById(bookingId);
        Booking updatedBooking = bookingService.cancel(booking);
        return bookingMapper.mapToBookingResponse(updatedBooking);
    }
}
