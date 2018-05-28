package ru.romanov.tonkoslovie.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.tonkoslovie.user.web.response.ValidationError;

import java.util.List;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends RuntimeException {

    private List<ValidationError> validationErrors;

}
