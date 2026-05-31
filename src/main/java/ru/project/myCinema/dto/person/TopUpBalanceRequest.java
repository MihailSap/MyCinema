package ru.project.myCinema.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Запрос на пополнение баланса")
public record TopUpBalanceRequest(Double amount) {
}
