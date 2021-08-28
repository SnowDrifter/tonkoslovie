package ru.romanov.tonkoslovie.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.user.UserService;
import ru.romanov.tonkoslovie.user.annotation.CurrentUserId;
import ru.romanov.tonkoslovie.user.dto.UserDto;
import ru.romanov.tonkoslovie.user.dto.UserMapper;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<UserDto> getUser(@CurrentUserId Long userId) {
        Optional<User> user = userRepository.findById(userId);

        return user.map(UserMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/users")
    public Page<UserDto> users(@RequestParam(defaultValue = "0") @Min(0) int page,
                               @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper.INSTANCE::toDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        UserResponse response = userService.login(request);

        HttpStatus responseStatus = StringUtils.hasText(response.getErrorMessage()) ? BAD_REQUEST : OK;
        return new ResponseEntity<>(response, responseStatus);
    }

    @PostMapping("/registration")
    public void processRegistration(@RequestBody UserDto userDto) {
        User user = UserMapper.INSTANCE.toEntity(userDto);
        userService.saveNewUser(user);
    }

    @GetMapping("/registration/confirm")
    public void confirmRegistration(@RequestParam("token") UUID token, HttpServletResponse response) throws IOException {
        userService.confirmRegistration(token, response);
    }

    @PostMapping("/update")
    public UserDto updateUser(@CurrentUserId Long userId, @RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

}
