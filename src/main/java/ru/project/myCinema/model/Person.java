package ru.project.myCinema.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Пользователь
 */
@Entity
@Table(name = "person")
public class Person extends BaseEntity{

    /**
     * Логин
     */
    private String login;

    /**
     * Имя
     */
    private String name;

    /**
     * Фамилия
     */
    private String surname;

    /**
     * Пароль
     */
    private String password;

    /**
     * Баланс
     */
    private Double balance;

    @Enumerated(EnumType.STRING)
    private PersonAccountStatus accountStatus;

    /**
     * Заказы
     */
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public PersonAccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(PersonAccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
