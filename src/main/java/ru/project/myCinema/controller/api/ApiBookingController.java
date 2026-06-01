package ru.project.myCinema.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.booking.BookingCreateRequest;
import ru.project.myCinema.dto.booking.BookingResponse;
import ru.project.myCinema.exception.BadRequestException;
import ru.project.myCinema.exception.ConflictException;
import ru.project.myCinema.mapper.BookingMapper;
import ru.project.myCinema.model.*;
import ru.project.myCinema.service.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Эндпоинты для управления заказами")
@RestController
@RequestMapping("/api/bookings")
public class ApiBookingController {

    private final AuthService authService;
    private final BookingService bookingService;
    private final SessionService sessionService;
    private final PersonService personService;
    private final SeatService seatService;
    private final BookingMapper bookingMapper;

    @Autowired
    public ApiBookingController(
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

    @Operation(description = "Получение заказа по id")
    @GetMapping("/{bookingId}")
    public BookingResponse getById(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingService.getById(bookingId);
        return bookingMapper.mapToBookingResponse(booking);
    }

    @Operation(description = "Создание заказа")
    @PostMapping
    public BookingResponse create(@Valid @RequestBody BookingCreateRequest bookingCreateRequest){
        Person person = authService.getAuthenticatedPerson();
        Session session = sessionService.getById(bookingCreateRequest.sessionId());
        Seat seat = seatService.getById(bookingCreateRequest.seatId());
        if(session.getStartDateTime().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Нельзя забронировать место на прошедший сеанс");
        }
        if(!seat.getHall().getId().equals(session.getHall().getId())){
            throw new BadRequestException("Место не принадлежит залу данного сеанса");
        }
        if(seatService.isSeatBooked(seat, session)){
            throw new ConflictException("Место занято");
        }

        Booking booking = bookingService.create(person, session, seat);
        return bookingMapper.mapToBookingResponse(booking);
    }

    @Operation(description = "Получение всех неоплаченных заказов авторизованного пользователя")
    @GetMapping("/actual/pending")
    public List<BookingResponse> getMyPendingActual(){
        Person person = authService.getAuthenticatedPerson();
        List<Booking> bookings = bookingService.getPendingActualByPerson(person);
        return bookingMapper.mapToBookingResponses(bookings);
    }

    @Operation(description = "Получение всех оплаченных заказов авторизованного пользователя")
    @GetMapping("/actual/done")
    public List<BookingResponse> getMyDoneActual(){
        Person person = authService.getAuthenticatedPerson();
        List<Booking> bookings = bookingService.getDoneActualByPerson(person);
        return bookingMapper.mapToBookingResponses(bookings);
    }

    @Operation(description = "Оплата заказа")
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

    @Operation(description = "Отмена заказа")
    @PatchMapping("/{bookingId}/cancel")
    public BookingResponse cancel(@PathVariable("bookingId") Long bookingId){
        Booking booking = bookingService.getById(bookingId);
        Booking updatedBooking = bookingService.cancel(booking);
        return bookingMapper.mapToBookingResponse(updatedBooking);
    }
}
