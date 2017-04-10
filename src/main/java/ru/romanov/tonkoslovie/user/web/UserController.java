package ru.romanov.tonkoslovie.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    // Proxy to React-Router
    @RequestMapping(value = "*", method = RequestMethod.GET)
    public String home() {
        return "index";
    }
}
