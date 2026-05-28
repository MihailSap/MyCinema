package ru.project.myCinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.DefaultResponse;
import ru.project.myCinema.dto.AuthRequest;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.PersonService;

/**
 * Контроллер с эндпоинтами для регистрации, входа и выхода пользователя
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PersonService personService;

    @Autowired
    public AuthController(AuthService authService, PersonService personService) {
        this.authService = authService;
        this.personService = personService;
    }

    /**
     * Регистрация нового пользователя
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public DefaultResponse register(@RequestBody AuthRequest authRequest){
        if(personService.isExistsByLogin(authRequest.login())){
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        personService.create(authRequest);
        return new DefaultResponse("Регистрация прошла успешно");
    }

    /**
     * Вход в аккаунт
     */
    @PostMapping("/login")
    public DefaultResponse login(@RequestBody AuthRequest authRequest, HttpSession session){
        UsernamePasswordAuthenticationToken authenticationInputToken = new UsernamePasswordAuthenticationToken(
                authRequest.login(), authRequest.password()
        );
        authService.login(authenticationInputToken, session);
        Person person = authService.getAuthenticatedPerson();
        return new DefaultResponse("Добро пожаловать, %s".formatted(person.getLogin()));
    }

    /**
     * Выход из аккаунта
     */
    @PostMapping("/logout")
    public DefaultResponse logout(HttpSession session){
        authService.logout(session);
        return new DefaultResponse("Вы вышли из аккаунта");
    }
}
