package ru.romanov.tonkoslovie.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.romanov.tonkoslovie.user.web.response.ValidationError;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {

    private List<ValidationError> validationErrors;

}
