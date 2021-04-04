package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileMapper fileMapper;
    private final CredentialMapper credentialMapper;
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public HomeController(FileMapper fileMapper, CredentialMapper credentialMapper, NoteMapper noteMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.credentialMapper = credentialMapper;
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model) {
        User user = userMapper.getUser(authentication.getName());
        if (user != null) {
            Integer userId = user.getUserId();
            model.addAttribute("files", fileMapper.getFilesByUserId(userId));
            model.addAttribute("notes", noteMapper.getNotesByUserId(userId));
            model.addAttribute("credentials", credentialMapper.getCredentialsByUserId(userId));
        }
        return "home";
    }
}
