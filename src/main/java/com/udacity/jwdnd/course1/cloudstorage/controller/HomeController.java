package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final FileService fileService;
    private final CredentialService credentialService;
    private final NoteService noteService;
    private final UserService userService;

    public HomeController(FileService fileService, CredentialService credentialService, NoteService noteService, UserService userService) {
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName());
        if (user != null) {
            Integer userId = user.getUserId();
            model.addAttribute("files", fileService.getFilesByUserId(userId));
            model.addAttribute("notes", noteService.getAllNotesByUserId(userId));
            model.addAttribute("credentials", credentialService.getAllCredentialsByUserId(userId));
        }
        return "home";
    }

    @GetMapping
    public String redirectToHome() {
        return "redirect:/home";
    }
}
