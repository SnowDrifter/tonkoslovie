package ru.romanov.tonkoslovie.model.mail;

import lombok.Data;

@Data
public class MailMessage {

    private String username;
    private String address;
    private String token;
    private String responseHost;

}
