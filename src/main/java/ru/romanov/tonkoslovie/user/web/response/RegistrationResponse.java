package ru.romanov.tonkoslovie.user.web.response;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationResponse {

    private List<ValidationError> validationErrors;
    private String errorMessage;

    public RegistrationResponse() {
    }

    public RegistrationResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RegistrationResponse(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
