package ru.project.myCinema.service.Impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.security.UserDetailsImpl;
import ru.project.myCinema.service.AuthService;

/**
 * Сервис, отвечающий за аутентификацию пользователей.
 * Используется для регистрации, входа и выхода
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void login(UsernamePasswordAuthenticationToken authenticationInputToken, HttpSession session){
        Authentication authentication = authenticationManager.authenticate(authenticationInputToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    @Override
    public void logout(HttpSession session){
        session.invalidate();
        SecurityContextHolder.clearContext();
    }

    @Override
    public Person getAuthenticatedPerson(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getPerson();
    }
}
