package ru.romanov.tonkoslovie.user;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.RegistrationResponse;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;

public interface UserService extends UserDetailsService {

    ResponseEntity<UserResponse> login(UserRequest request);

    ResponseEntity<RegistrationResponse> saveNewUser(User user);

    User update(User user);

    boolean checkToken(String token);

}
