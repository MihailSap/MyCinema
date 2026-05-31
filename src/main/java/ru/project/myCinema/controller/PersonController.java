package ru.project.myCinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.myCinema.dto.BookingResponse;
import ru.project.myCinema.dto.TopUpBalanceRequest;
import ru.project.myCinema.dto.UpdatePersonRequest;
import ru.project.myCinema.mapper.BookingMapper;
import ru.project.myCinema.model.Booking;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.BookingService;
import ru.project.myCinema.service.PersonService;

import java.util.List;

/**
 * Контроллер для работы с пользователями
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    private final AuthService authService;
    private final PersonService personService;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    @Autowired
    public PersonController(
            AuthService authService,
            PersonService personService, BookingMapper bookingMapper, BookingService bookingService
    ) {
        this.authService = authService;
        this.personService = personService;
        this.bookingMapper = bookingMapper;
        this.bookingService = bookingService;
    }

    /**
     * Страница текущего пользователя
     */
    @GetMapping("/me")
    public String profilePage(Model model) {
        Person person = authService.getAuthenticatedPerson();
        model.addAttribute("person", person);

        List<Booking> bookings = bookingService.getPendingActualByPerson(person);
        List<BookingResponse> bookingResponses = bookingMapper.mapToBookingResponses(bookings);
        model.addAttribute("pendingBookings", bookingResponses);

        List<Booking> doneBookings = bookingService.getDoneActualByPerson(person);
        List<BookingResponse> doneBookingResponses = bookingMapper.mapToBookingResponses(doneBookings);
        model.addAttribute("doneBookings", doneBookingResponses);

        return "person/details";
    }

    /**
     * Страница пополнения баланса
     */
    @GetMapping("/me/balance")
    public String topUpBalancePage(Model model) {
        model.addAttribute("topUpBalanceRequest", new TopUpBalanceRequest(null));
        return "person/edit-balance";
    }

    /**
     * Пополнение баланса
     */
    @PostMapping("/me/balance")
    public String topUpBalance(
            @ModelAttribute TopUpBalanceRequest topUpBalanceRequest
    ) {
        Person person = authService.getAuthenticatedPerson();
        personService.topUpBalance(person, topUpBalanceRequest.amount());
        return "redirect:/persons/me";
    }

    /**
     * Смена роли пользователя
     */
    @PostMapping("/me/role")
    public String changeRole() {
        Person person = authService.getAuthenticatedPerson();
        Person updatedPerson = personService.changeRole(person);
        authService.refreshAuthentication(updatedPerson);
        return "redirect:/persons/me";
    }

    /**
     * Страница обновления профиля
     */
    @GetMapping("/me/update")
    public String updatePage(Model model) {
        Person person = authService.getAuthenticatedPerson();
        UpdatePersonRequest updatePersonRequest =
                new UpdatePersonRequest(
                        person.getLogin(),
                        person.getName(),
                        person.getSurname()
                );
        model.addAttribute("updatePersonRequest", updatePersonRequest);

        return "person/edit";
    }

    /**
     * Обновление профиля пользователя
     */
    @PostMapping("/me/update")
    public String update(@ModelAttribute UpdatePersonRequest updatePersonRequest) {
        Person person = authService.getAuthenticatedPerson();
        Person updatedPerson = personService.update(updatePersonRequest, person);
        authService.refreshAuthentication(updatedPerson);
        return "redirect:/persons/me";
    }
}