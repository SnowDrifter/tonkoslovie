package ru.romanov.tonkoslovie.mail;

import ru.romanov.tonkoslovie.user.entity.User;

public interface EmailService {

    void sendVerification(User user);

}
