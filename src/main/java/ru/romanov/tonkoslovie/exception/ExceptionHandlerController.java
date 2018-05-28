package ru.romanov.tonkoslovie.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.tonkoslovie.exception.response.ErrorResponse;
import ru.romanov.tonkoslovie.exception.response.ValidationErrorResponse;
import ru.romanov.tonkoslovie.user.exception.ValidationException;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse mainHandler(Exception exception) {
        log.error("Main exception handler, class={} message={}", exception.getClass().getSimpleName(), exception.getMessage());
        return new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ValidationErrorResponse validationException(ValidationException exception) {
        log.error("Validation exception handler, errors={}", exception.getValidationErrors());
        return new ValidationErrorResponse(exception.getValidationErrors());
    }

}
