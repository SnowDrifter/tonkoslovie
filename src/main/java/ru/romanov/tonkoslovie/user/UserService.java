package ru.romanov.tonkoslovie.user;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserResponse login(UserRequest request);

    void saveNewUser(User user);

    User update(long userId, UserRequest request);

    void confirmRegistration(UUID token, HttpServletResponse response) throws IOException;

}
