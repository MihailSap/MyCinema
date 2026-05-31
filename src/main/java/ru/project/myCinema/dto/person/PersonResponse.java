package ru.project.myCinema.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.myCinema.model.PersonAccountStatus;
import ru.project.myCinema.model.Role;

@Schema(title = "Данные пользователя для передачи клиенту")
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
