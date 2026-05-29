package ru.project.myCinema.dto;

import ru.project.myCinema.model.PersonAccountStatus;
import ru.project.myCinema.model.Role;

/**
 * Данные пользователя для передачи клиенту
 */
public record PersonResponse(
        Long id,
        String login,
        String name,
        String surname,
        Double balance,
        PersonAccountStatus accountStatus,
        Role role
) {
}
