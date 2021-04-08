package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;


@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final Logger logger = LoggerFactory.getLogger(CredentialController.class);
    private final MessageSource messageSource;
    private final CredentialService credentialService;
    private final UserService userService;
    private final String SUCCESS_MESSAGE = "credentialSuccessMessage";
    private final String ERROR_MESSAGE = "credentialErrorMessage";

    public CredentialController(CredentialService credentialService, UserService userService,
                                MessageSource messageSource) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping("/save")
    public String saveCredential(@ModelAttribute("credential") Credential credential, Authentication authentication,
                                 RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();

        String errorMessage;
        String successMessage;

        if (credential == null) {
            errorMessage = messageSource.getMessage("credentials-tab.credential-empty-form-msg",
                    null, Locale.getDefault());
            ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
            logger.error("Credential error: " + errorMessage + " userid = " + userId);
            return "redirect:/home#nav-credentials";
        } else if (credentialService.exists(credential, userId)) {
            errorMessage = messageSource.getMessage("credentials-tab.credential-already-exists-msg",
                    null, Locale.getDefault());
            ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
            logger.error("Credential error: " + errorMessage + " userid = " + userId);
            return "redirect:/home#nav-credentials";
        }
        if (credential.getCredentialId() == null) {
            int noteId;
            credential.setUserId(userId);
            noteId = credentialService.save(credential);
            if (noteId != 1) {
                errorMessage = messageSource.getMessage("credentials-tab.credential-save-error-msg",
                        null, Locale.getDefault());
                ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
                logger.debug("Credential error: " + errorMessage + " userid = " + userId);
            } else {
                successMessage = messageSource.getMessage("credentials-tab.credential-save-success-msg",
                        null, Locale.getDefault());
                ra.addFlashAttribute(SUCCESS_MESSAGE, successMessage);
                ra.addFlashAttribute("credentials", credentialService.getAllCredentialsByUserId(user.getUserId()));
            }
        } else {
            credential.setUserId(user.getUserId());
            int updated = credentialService.update(credential, userId);
            if (updated != 1) {
                errorMessage = messageSource.getMessage("credentials-tab.credential-update-error-msg",
                        null, Locale.getDefault());
                ra.addFlashAttribute(ERROR_MESSAGE, errorMessage);
                logger.debug("Credential error: " + errorMessage + " userid = " + userId);
            } else {
                successMessage = messageSource.getMessage("credentials-tab.credential-update-success-msg",
                        null, Locale.getDefault());
                ra.addFlashAttribute(SUCCESS_MESSAGE, successMessage);
                ra.addFlashAttribute("credentials", credentialService.getAllCredentialsByUserId(user.getUserId()));
            }
        }
        return "redirect:/home#nav-credentials";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id, Authentication authentication,
                             RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        int deleted = credentialService.deleteById(id, userId);
        if (deleted == 0){
            logger.error("Credential with id = " + id + " was not deleted");
            ra.addFlashAttribute(ERROR_MESSAGE, messageSource.getMessage(
                    "credentials-tab.credential-delete-error-msg", null, Locale.getDefault()
            ));
        } else {
            ra.addFlashAttribute(SUCCESS_MESSAGE, messageSource.getMessage(
                    "credentials-tab.credential-delete-success-msg", null, Locale.getDefault()
            ));
        }
        ra.addFlashAttribute("files", credentialService.getAllCredentialsByUserId(user.getUserId()));
        return "redirect:/home#nav-credentials";
    }
}
