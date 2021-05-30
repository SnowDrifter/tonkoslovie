package ru.romanov.tonkoslovie.mail.service;


import ru.romanov.tonkoslovie.model.mail.MailMessage;

public interface MailService {

    void sendMail(MailMessage mailMessage);

}
