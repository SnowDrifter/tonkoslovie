package ru.romanov.tonkoslovie.mail;

import ru.romanov.tonkoslovie.user.entity.User;

import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    void sendVerification(User user) throws UnsupportedEncodingException, AddressException;

}