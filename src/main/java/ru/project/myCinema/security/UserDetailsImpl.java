package ru.project.myCinema.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.project.myCinema.model.Person;

import java.util.Collection;
import java.util.List;

/**
 * Реализация интерфейса UserDetails.
 * Отвечает за представление аутентифицированного пользователя в Spring Security.
 */
public class UserDetailsImpl implements UserDetails {

    private final Person person;

    public UserDetailsImpl(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + person.getRole().name()));
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Получение пользователя
     */
    public Person getPerson(){
        return person;
    }
}
