package ru.romanov.tonkoslovie.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.user.UserService;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.RegistrationResponse;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        return userService.login(request);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<RegistrationResponse> processRegistration(@RequestBody User user) {
        return userService.saveNewUser(user);
    }

    @RequestMapping(value = "/confirmRegistration", method = RequestMethod.GET)
    public void confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        if (userService.checkToken(token)) {
            response.sendRedirect("https://yandex.ru");
        } else {
            response.sendRedirect("https://google.ru");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }
}
