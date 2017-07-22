package ru.romanov.tonkoslovie.mail;

import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendVerification(User user) throws UnsupportedEncodingException, AddressException, CannotSendEmailException;

}
