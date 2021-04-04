package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
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
                int updated = credentialService.update(credential);
                if (updated > 0) {
                    ra.addFlashAttribute("credentialUpdateSuccess", true);
                    ra.addFlashAttribute("credentials", credentialService.getAllCredentialsByUserId(user.getUserId()));
                } else {
                    ra.addFlashAttribute("credentialUpdateError", true);
                }
            }
        }
        return "redirect:/home";
    }
}
