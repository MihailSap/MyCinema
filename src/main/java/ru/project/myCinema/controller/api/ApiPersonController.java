package ru.project.myCinema.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.DefaultResponse;
import ru.project.myCinema.dto.person.PersonResponse;
import ru.project.myCinema.dto.person.TopUpBalanceRequest;
import ru.project.myCinema.dto.person.UpdatePersonRequest;
import ru.project.myCinema.mapper.PersonMapper;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.model.PersonAccountStatus;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.PersonService;

import java.util.List;

@Tag(name = "Эндпоинты для работы с данными пользователей")
@RestController
@RequestMapping("/api/persons")
public class ApiPersonController {

    private final AuthService authService;
    private final PersonService personService;
    private final PersonMapper personMapper;

    @Autowired
    public ApiPersonController(
            AuthService authService,
            PersonService personService,
            PersonMapper personMapper
    ) {
        this.authService = authService;
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @Operation(description = "Получение данных текущего пользователя")
    @GetMapping("/me")
    public PersonResponse getCurrentPerson() {
        Person person = authService.getAuthenticatedPerson();
        return personMapper.mapToPersonResponse(person);
    }

    @Operation(description = "Получение данных всех существующих пользователей")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personService.getAll();
        return personMapper.mapToPersonResponses(persons);
    }

    @Operation(description = "Получение данных пользователя по его id")
    @GetMapping("/{personId}")
    public PersonResponse getPersonById(@PathVariable("personId") Long personId) {
        Person person = personService.getById(personId);
        return personMapper.mapToPersonResponse(person);
    }

    @Operation(description = "Обновление данных авторизованного пользователя")
    @PatchMapping("/me")
    public PersonResponse update(@RequestBody UpdatePersonRequest updatePersonRequest){
        Person person = authService.getAuthenticatedPerson();
        Person updatedPerson = personService.update(updatePersonRequest, person);
        return personMapper.mapToPersonResponse(updatedPerson);
    }

    @Operation(description = "Выход с последующим удалением аккаунта авторизованного пользователя")
    @DeleteMapping("/me")
    public DefaultResponse delete(HttpSession session){
        Person person = authService.getAuthenticatedPerson();
        authService.logout(session);
        personService.delete(person);
        return new DefaultResponse("Вы удалили свой аккаунт");
    }

    @Operation(description = "Пополнение баланса авторизованного пользователя")
    @PostMapping("/me/balance")
    public PersonResponse topUpBalance(@RequestBody TopUpBalanceRequest topUpBalanceRequest){
        if(topUpBalanceRequest.amount() <= 0){
            throw new RuntimeException("Сумма для пополнения баланса должна быть больше нуля");
        }
        Person person = authService.getAuthenticatedPerson();
        Person updatedPerson = personService.topUpBalance(person, topUpBalanceRequest.amount());
        return personMapper.mapToPersonResponse(updatedPerson);
    }

    @Operation(description = "Блокировка пользователя по id")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{personId}/block")
    public PersonResponse block(@PathVariable("personId") Long personId){
        Person person = personService.getById(personId);
        if(PersonAccountStatus.BLOCKED.equals(person.getAccountStatus())){
            throw new RuntimeException("Указанный пользователь заблокирован");
        }
        Person updatedPerson = personService.block(person);
        return personMapper.mapToPersonResponse(updatedPerson);
    }

    @Operation(description = "Разблокировка пользователя по id")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{personId}/unblock")
    public PersonResponse unblock(@PathVariable("personId") Long personId){
        Person person = personService.getById(personId);
        if(PersonAccountStatus.ACTIVE.equals(person.getAccountStatus())){
            throw new RuntimeException("Указанный пользователь не заблокирован");
        }
        Person updatedPerson = personService.unblock(person);
        return personMapper.mapToPersonResponse(updatedPerson);
    }

    @Operation(description = "Обновление роли пользователя")
    @PatchMapping("/me/role")
    public PersonResponse changeRole(){
        Person person = authService.getAuthenticatedPerson();
        Person updatePerson = personService.changeRole(person.getId());
        authService.refreshAuthentication(updatePerson);
        return personMapper.mapToPersonResponse(updatePerson);
    }
}
