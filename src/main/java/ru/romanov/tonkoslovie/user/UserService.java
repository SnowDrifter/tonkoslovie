package ru.romanov.tonkoslovie.user;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romanov.tonkoslovie.user.entity.User;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    void saveNewUser(User user);

    User update(User user);

    long countByUsername(String username);

    boolean checkToken(String token);
}
