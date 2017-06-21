package ru.romanov.tonkoslovie.user.web.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserResponse {

    private String token;
    private String errorMessage;

}
