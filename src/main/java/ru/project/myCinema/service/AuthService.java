package ru.project.myCinema.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.project.myCinema.model.Person;

/**
 * Интерфейс для работы с аутентификацией
 */
public interface AuthService {

    /**
     * Вход пользователя в аккаунт
     */
    void login(UsernamePasswordAuthenticationToken authenticationInputToken, HttpSession session);

    /**
     * Выход пользователя из аккаунта
     */
    void logout(HttpSession session);

    /**
     * Получение авторизованного пользователя
     */
    Person getAuthenticatedPerson();
}
