package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final Logger logger = LoggerFactory.getLogger(CredentialController.class);
    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String saveCredential(@ModelAttribute("credential") Credential credential, Authentication authentication,
                                 RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        if (credential == null) {
            ra.addFlashAttribute("credentialSaveError", true);
        } else {
            if (credential.getCredentialId() == null) {
                int noteId;
                credential.setUserId(userId);
                noteId = credentialService.save(credential);
                if (noteId < 1) ra.addFlashAttribute("credentialSaveError", true);
                else {
                    ra.addFlashAttribute("credentialSaveSuccess", true);
                    ra.addFlashAttribute("credentials", credentialService.getAllCredentialsByUserId(user.getUserId()));
                }
            } else {
                credential.setUserId(user.getUserId());
                int updated = credentialService.update(credential, userId);
                if (updated > 0) {
                    ra.addFlashAttribute("credentialUpdateSuccess", true);
                    ra.addFlashAttribute("credentials", credentialService.getAllCredentialsByUserId(user.getUserId()));
                } else {
                    ra.addFlashAttribute("credentialUpdateError", true);
                }
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
            ra.addFlashAttribute("credentialDeleteError", true);
        } else {
            ra.addFlashAttribute("credentialDeleteSuccess", true);
        }
        ra.addFlashAttribute("files", credentialService.getAllCredentialsByUserId(user.getUserId()));
        return "redirect:/home#nav-credentials";
    }
}
