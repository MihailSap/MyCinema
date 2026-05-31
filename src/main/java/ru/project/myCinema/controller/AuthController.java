package ru.project.myCinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.myCinema.dto.AuthRequest;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.PersonService;

/**
 * Контроллер для работы с аутентификацией
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PersonService personService;

    @Autowired
    public AuthController(
            AuthService authService,
            PersonService personService
    ) {
        this.authService = authService;
        this.personService = personService;
    }

    /**
     * Страница входа
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("authRequest", new AuthRequest(null, null));
        return "auth/login";
    }

    /**
     * Страница регистрации
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("authRequest", new AuthRequest(null, null));
        return "auth/register";
    }

    /**
     * Обработка регистрации
     */
    @PostMapping("/register")
    public String register(@ModelAttribute AuthRequest authRequest, Model model) {
        if (personService.isExistsByLogin(authRequest.login())) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "auth/register";
        }
        personService.create(authRequest);
        return "redirect:/auth/login";
    }

    /**
     * Выход
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/auth/login";
    }
}