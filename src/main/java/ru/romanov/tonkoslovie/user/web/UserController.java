package ru.romanov.tonkoslovie.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.user.UserService;
import ru.romanov.tonkoslovie.user.annotation.CurrentUserId;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.RegistrationResponse;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        return userService.login(request);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<RegistrationResponse> processRegistration(@RequestBody User user) {
        return userService.saveNewUser(user);
    }

    @RequestMapping(value = "/confirmRegistration", method = RequestMethod.GET)
    public void confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        userService.confirmRegistration(token, response);
    }

    @GetMapping("/users")
    public List<User> users() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @GetMapping
    public ResponseEntity<User> getUser(@CurrentUserId Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

}
