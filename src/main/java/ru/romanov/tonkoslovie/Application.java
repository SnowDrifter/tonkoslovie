package ru.romanov.tonkoslovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

@Controller
@ComponentScan(basePackages = {"ru.romanov", "it.ozimov.springboot"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home() throws AddressException, UnsupportedEncodingException {
        return "home";
    }
}