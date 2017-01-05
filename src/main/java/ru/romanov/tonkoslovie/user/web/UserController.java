package ru.romanov.tonkoslovie.user.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.romanov.tonkoslovie.user.UserService;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("title", messageSource.getMessage("home.title", null, null));
        return "home";
    }

    @RequestMapping(value = "/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", messageSource.getMessage("login.fail", null, null));
        }

        model.addAttribute("title", messageSource.getMessage("login.title", null, null));
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistration(Model model) {
        model.addAttribute("title", messageSource.getMessage("registration.title", null, null));
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String processRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("error", messageSource.getMessage("registration.fail", null, null));
            return "registration";
        }

        // Already registered
        if (userService.countByUsername(user.getUsername()) > 0) {
            model.addAttribute("error", messageSource.getMessage("registration.userExist", null, null));
            model.addAttribute("user", user);
            return "registration";
        }

        userService.saveNewUser(user);
        model.addAttribute("message", messageSource.getMessage("registration.done", null, null));

        return "accept";
    }

    @RequestMapping(value = "/confirmRegistration", method = RequestMethod.GET)
    public String confirmRegistration(@RequestParam("token") String token, Model model) {

        if (userService.checkToken(token)) {
            return "redirect:/home";
        } else {
            model.addAttribute("message", messageSource.getMessage("registration.mail.confirm", null, null));
            return "redirect:/accept";
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@Valid User user, BindingResult bindingResult, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            logger.info("Result has errors:");
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.info(error.toString());
            }

            // TODO
            model.addAttribute("title", messageSource.getMessage("edit.title", null, null));
            model.addAttribute("user", user);
            return "edit-profile";
        }

        userService.update(user);

        model.addAttribute("message", messageSource.getMessage("edit.done", null, null));
        return "edit-profile";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showEdit(Model model, Principal principal) {

        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("title", messageSource.getMessage("edit.title", null, null));

        return "edit-profile";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/home";
    }
}
