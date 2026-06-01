package ru.project.myCinema.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.project.myCinema.dto.DefaultResponse;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Обрабатывает некорректные запросы
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultResponse> handleBadRequestEx(BadRequestException ex) {
        logger.error("BadRequestException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new DefaultResponse(ex.getMessage()));
    }

    /**
     * Обрабатывает ситуации, в которых доступ запрещён
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<DefaultResponse> handleForbiddenEx(ForbiddenException ex) {
        logger.error("ForbiddenException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new DefaultResponse(ex.getMessage()));
    }

    /**
     * Обрабатывает конфликты
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<DefaultResponse> handleConflictEx(ConflictException ex) {
        logger.error("ConflictException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new DefaultResponse(ex.getMessage()));
    }

    /**
     * Обрабатывает, когда отсутствует объект из базы данных
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultResponse> handleNotFoundEx(NotFoundException ex) {
        logger.error("NotFoundException: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DefaultResponse(ex.getMessage()));
    }

    /**
     * Обрабатывает ошибки валидации входных данных
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse> handleMethodArgumentNotValidEx(MethodArgumentNotValidException ex) {
        String description = "%s %s".formatted("Неправильно заполнены поля.",
                ex.getBindingResult().getFieldErrors().stream().map((fe) -> {
                    String fieldName = fe.getField();
                    return fieldName + ": " + fe.getDefaultMessage();
                }).collect(Collectors.joining(", ")));
        logger.error("ValidationException: {}", description, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new DefaultResponse(description));
    }

    /**
     * Обрабатывает все неожиданные ошибки, не попавшие под другие обработчики
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse> handleInternalServerEx(Exception ex) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DefaultResponse(ex.getMessage()));
    }
}
