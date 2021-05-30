package ru.romanov.tonkoslovie.user.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {

    private String field;
    private String message;

}
