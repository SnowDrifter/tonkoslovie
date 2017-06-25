package ru.romanov.tonkoslovie.user.web.response;

import lombok.Data;

@Data
public class RegistrationResponse {

    private String errorMessage;

    public RegistrationResponse() {
    }

    public RegistrationResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
