package ru.project.myCinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Ответ по умолчанию")
public record DefaultResponse(String message) {
}
