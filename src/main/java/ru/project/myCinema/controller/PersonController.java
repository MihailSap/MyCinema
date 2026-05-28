package ru.project.myCinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.DefaultResponse;
import ru.project.myCinema.dto.PersonResponse;
import ru.project.myCinema.dto.UpdatePersonRequest;
import ru.project.myCinema.mapper.PersonMapper;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.PersonService;

import java.util.List;

/**
 * Контроллер для работы с данными пользователей
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final AuthService authService;
    private final PersonService personService;
    private final PersonMapper personMapper;

    @Autowired
    public PersonController(
            AuthService authService,
            PersonService personService,
            PersonMapper personMapper
    ) {
        this.authService = authService;
        this.personService = personService;
        this.personMapper = personMapper;
    }

    /**
     * Получение данных текущего пользователя
     */
    @GetMapping("/me")
    public PersonResponse getCurrentPerson() {
        Person person = authService.getAuthenticatedPerson();
        return personMapper.mapToPersonResponse(person);
    }

    /**
     * Получение данных всех существующих пользователей
     */
    @GetMapping("/all")
    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personService.getAll();
        return personMapper.mapToPersonResponses(persons);
    }

    /**
     * Получение данных пользователя по его id
     */
    @GetMapping("/{personId}")
    public PersonResponse getPersonById(@PathVariable("personId") Long personId) {
        Person person = personService.getById(personId);
        return personMapper.mapToPersonResponse(person);
    }

    /**
     * Обновление данных авторизованного пользователя
     */
    @PatchMapping("/me")
    public PersonResponse update(@RequestBody UpdatePersonRequest updatePersonRequest){
        Person person = authService.getAuthenticatedPerson();
        Person updatedPerson = personService.update(updatePersonRequest, person);
        return personMapper.mapToPersonResponse(updatedPerson);
    }

    /**
     * Выход с последующим удалением аккаунта авторизованного пользователя
     */
    @DeleteMapping("/me")
    public DefaultResponse delete(HttpSession session){
        Person person = authService.getAuthenticatedPerson();
        authService.logout(session);
        personService.delete(person);
        return new DefaultResponse("Вы удалили свой аккаунт");
    }
}
