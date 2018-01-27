package ru.romanov.tonkoslovie.user.web.request;


import lombok.Data;

@Data
public class UserRequest {

    private String email;
    private String password;

}
