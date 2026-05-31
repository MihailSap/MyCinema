package ru.project.myCinema.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.project.myCinema.model.Person;
import ru.project.myCinema.security.UserDetailsImpl;
import ru.project.myCinema.service.PersonService;

/**
 * Реализация интерфейса UserDetailsService.
 * Используется Spring Security для получения информации о пользователе в виде объекта UserDetails
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonService personService;

    @Autowired
    public UserDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Person person = personService.getByLogin(login);
        return new UserDetailsImpl(person);
    }
}
