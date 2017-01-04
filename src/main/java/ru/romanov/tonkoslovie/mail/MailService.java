package ru.romanov.tonkoslovie.mail;

public interface MailService {

    void sendVerification(String toAddress, String token);

}
