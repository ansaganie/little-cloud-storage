package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(SignupController.class);
    private final MessageSource messageSource;

    public SignupController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model, RedirectAttributes ra) {
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = messageSource.getMessage(
                    "signup-page.username-exits-error-msg",
                    null,
                    Locale.getDefault());
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = messageSource.getMessage(
                        "signup-page.signup-error-msg",
                        null,
                        Locale.getDefault()
                );
            }
        }

        if (signupError == null) {
            ra.addFlashAttribute("signupSuccess", true);
            logger.debug("USER username = " + user.getUsername() + " was signed up");
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupError);
            logger.error("User username = " + user.getUsername() + "error: " + signupError);
        }

        return "signup";
    }
}
