package ru.project.myCinema.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.project.myCinema.dto.person.AuthRequest;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.service.AuthService;
import ru.project.myCinema.service.PersonService;

/**
 * Тесты для контроллера аутентификации
 */
@WebMvcTest(ApiAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApiAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private PersonService personService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Проверяет регистрацию пользователя с уже существующим логином
     */
    @Test
    void testRegisterWithExistingLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "password");
        Mockito.when(personService.isExistsByLogin("user"))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Пользователь с таким логином уже существует"));

        Mockito.verify(personService).isExistsByLogin("user");
        Mockito.verify(personService, Mockito.never()).create(Mockito.any(AuthRequest.class));
    }

    /**
     * Проверяет успешную регистрацию пользователя
     */
    @Test
    void testRegisterWithNewLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "password");
        Mockito.when(personService.isExistsByLogin("user"))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Регистрация прошла успешно"));

        Mockito.verify(personService).isExistsByLogin("user");
        Mockito.verify(personService).create(authRequest);
    }

    /**
     * Проверяет успешный вход пользователя
     */
    @Test
    void testLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest("user123", "password");
        Person person = new Person();
        person.setLogin("user123");

        Mockito.doNothing()
                .when(authService)
                .login(Mockito.any(UsernamePasswordAuthenticationToken.class), Mockito.any(HttpSession.class));

        Mockito.when(authService.getAuthenticatedPerson())
                .thenReturn(person);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Добро пожаловать, user123"));

        Mockito.verify(authService).login(
                Mockito.any(UsernamePasswordAuthenticationToken.class), Mockito.any(HttpSession.class));
        Mockito.verify(authService).getAuthenticatedPerson();
    }

    /**
     * Проверяет успешный выход пользователя
     */
    @Test
    void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/logout"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Вы вышли из аккаунта"));

        Mockito.verify(authService).logout(Mockito.any(HttpSession.class));
    }
}