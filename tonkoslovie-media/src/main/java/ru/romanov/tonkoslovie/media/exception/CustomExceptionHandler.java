package ru.romanov.tonkoslovie.media.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.tonkoslovie.media.exception.response.ErrorResponse;

@Slf4j
@RestController
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse mainHandler(Exception exception) {
        log.error("Main exception handler, class={} message={}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
        return new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

}
