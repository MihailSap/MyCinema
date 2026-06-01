package ru.project.myCinema.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.DefaultResponse;
import ru.project.myCinema.dto.person.AuthRequest;
import ru.project.myCinema.exception.ConflictException;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.PersonService;

@Tag(name = "Эндпоинты для регистрации, входа и выхода")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthService authService;
    private final PersonService personService;

    @Autowired
    public ApiAuthController(AuthService authService, PersonService personService) {
        this.authService = authService;
        this.personService = personService;
    }

    @Operation(description = "Регистрация нового пользователя")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public DefaultResponse register(@Valid @RequestBody AuthRequest authRequest){
        if(personService.isExistsByLogin(authRequest.login())){
            throw new ConflictException("Пользователь с таким логином уже существует");
        }
        personService.create(authRequest);
        return new DefaultResponse("Регистрация прошла успешно");
    }

    @Operation(description = "Вход в аккаунт")
    @PostMapping("/login")
    public DefaultResponse login(@Valid @RequestBody AuthRequest authRequest, HttpSession session){
        UsernamePasswordAuthenticationToken authenticationInputToken = new UsernamePasswordAuthenticationToken(
                authRequest.login(), authRequest.password()
        );
        authService.login(authenticationInputToken, session);
        Person person = authService.getAuthenticatedPerson();
        return new DefaultResponse("Добро пожаловать, %s".formatted(person.getLogin()));
    }

    @Operation(description = "Выход из аккаунта")
    @PostMapping("/logout")
    public DefaultResponse logout(HttpSession session){
        authService.logout(session);
        return new DefaultResponse("Вы вышли из аккаунта");
    }
}

